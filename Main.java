import java.util.*;

class Main {

  public static void main(String[] args) {
    int desiredPublicKeyLength = 8;

    Scanner sc = new Scanner(System.in);
    Random r = new Random();

    //get text
    System.out.println();
    System.out.print("please enter text for encryption: ");
    String text = sc.nextLine();

    //get encryption or decryption
    String cipherType;
    do {
      System.out.print("please enter encryption or decryption: ");
      cipherType = sc.nextLine();
      cipherType = cipherType.toLowerCase();
    } while (
      !cipherType.contains("encrypt") && !cipherType.contains("decrypt")
    );

    //get public key /w 'desiredPublicKeyLength' long char passcode
    String publicKey = "";
    if (cipherType.contains("decrypt")) {
      do {
        System.out.print(
          "please enter " +
          desiredPublicKeyLength +
          " char password for encryption: "
        );
        publicKey = sc.nextLine();
      } while (publicKey.length() != desiredPublicKeyLength);
    } else {
      publicKey = "";
      for (int i = 0; i < desiredPublicKeyLength; i++) {
        int rand = r.nextInt(32, 126);
        char x = (char) rand; //generate random public key for encryption
        publicKey += x;
      }
      System.out.println(
        "the following public key must be used to decrypt ciphered text: " +
        publicKey
      );
    }

    //(94^keyLength) possible password combinations
    //get value of public key

    int ValueHolder[] = new int[desiredPublicKeyLength];
    for (int i = 0; i < desiredPublicKeyLength; i++) {
      for (int x = 32; x < 127; x++) {
        if ((int) publicKey.charAt(i) == x) {
          ValueHolder[i] += x - 31; //because of ASCII --> nth prime
        }
      }
    }

    //find the nth prime values
    int primeCount = 0;
    int primeHolder[] = new int[desiredPublicKeyLength];
    for (int i = 0; i < desiredPublicKeyLength; i++) {
      primeCount = 0;
      for (int l = 0; l < 503; l++) {
        if (isPrime(l) == true) {
          primeCount++;
          if (primeCount == ValueHolder[i]) {
            primeHolder[i] = l;
          }
        }
      }
    }

    //at this point primeHolder holds all of the nth prime values and value holder holds value(n)
    //take all values and split it up into groups of 2, add all groups of 2 together

    int producter = 1; // this var is used to store values of products of groups of 2
    int additioner = 0; // this var adds together all products
    for (int i = 0; i < desiredPublicKeyLength; i++) {
      producter *= primeHolder[i];

      if (i % 2 != 0) {
        additioner += producter;
        producter = 1;
      }
    }
    if (producter != 1) {
      additioner += producter;
    }

    //start cipher
    String Binary = Integer.toBinaryString(additioner);
    String zeros = "0000"; //String of zeroes
    if (!Binary.contains("0")) {
      Binary = zeros.substring(Binary.length()) + Binary;
    }

    System.out.print("your ciphered text: ");

    String asciiCharacters = "";
    for (int i = 32; i <= 126; i++) {
      asciiCharacters += (char) i;
    }

    String cipherText = "";
    int charPosition, keyVal;
    char replaceVal;

    for (int i = 0; i < text.length(); i++) {
      char xo = Binary.charAt(i % Binary.length());
      if (
        cipherType.contains("encrypt") &&
        xo == '1' ||
        cipherType.contains("decrypt") &&
        xo == '0'
      ) {
        charPosition = asciiCharacters.indexOf(text.charAt(i));
        keyVal =
          (
            (primeHolder[i % primeHolder.length] + charPosition) %
            asciiCharacters.length()
          );
        replaceVal = asciiCharacters.charAt(keyVal);
        cipherText += replaceVal;
      }
      if (
        cipherType.contains("encrypt") &&
        xo == '0' ||
        cipherType.contains("decrypt") &&
        xo == '1'
      ) {
        charPosition = asciiCharacters.indexOf(text.charAt(i));
        keyVal =
          (
            (charPosition - primeHolder[i % primeHolder.length]) %
            asciiCharacters.length()
          );
        if (keyVal < 0) {
          keyVal = asciiCharacters.length() + keyVal;
        }
        replaceVal = asciiCharacters.charAt(keyVal);
        cipherText += replaceVal;
      }
      //ENDFOR
    }

    System.out.println(cipherText);

    //stats
    System.out.println();
    System.out.println("stats: ");
    System.out.println();
    System.out.println("additioner as binary: " + Binary);
    System.out.println("passcode: " + publicKey);
    System.out.println("Value Holder Array: " + Arrays.toString(ValueHolder));
    System.out.println("primeHolder Array: " + Arrays.toString(primeHolder));
    System.out.println("Binary Value: " + additioner);
  }

  static boolean isPrime(int num) {
    if (num <= 1) {
      return false;
    }
    for (int i = 2; i <= num / 2; i++) {
      if ((num % i) == 0) return false;
    }
    return true;
  }
}
//shift + alt + f == cleaning up code shortcut
