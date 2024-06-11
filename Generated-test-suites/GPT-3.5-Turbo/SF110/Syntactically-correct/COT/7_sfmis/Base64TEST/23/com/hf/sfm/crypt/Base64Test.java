
import org.junit.Test;

public class Base64Test {
  
  @Test
  public void testAltBase64ToByteArray() {
    String s = "SGVsbG8gd29ybGQ=";
    byte[] expected = "Hello world".getBytes();
    byte[] actual = Base64.altBase64ToByteArray(s);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testBase64ToByteArray() {
    String s = "SGVsbG8gd29ybGQ=";
    byte[] expected = "Hello world".getBytes();
    byte[] actual = Base64.base64ToByteArray(s);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testByteArrayToAltBase64() {
    byte[] b = "Hello world".getBytes();
    String expected = "SGVsbG8gd29ybGQ=";
    String actual = Base64.byteArrayToAltBase64(b);
    assertEquals(expected, actual);
  }

  @Test
  public void testByteArrayToBase64() {
    byte[] b = "Hello world".getBytes();
    String expected = "SGVsbG8gd29ybGQ=";
    String actual = Base64.byteArrayToBase64(b);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAltBase64ToByteArrayInvalidLength() {
    String s = "SGVsbG8gd29ybG===";
    Base64.altBase64ToByteArray(s);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBase64ToByteArrayInvalidLength() {
    String s = "SGVsbG8gd29ybG===";
    Base64.base64ToByteArray(s);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAltBase64ToByteArrayInvalidCharacter() {
    String s = "SGVsbG8gd+#9ybGQ=";
    Base64.altBase64ToByteArray(s);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBase64ToByteArrayInvalidCharacter() {
    String s = "SGVsbG8gd+#9ybGQ=";
    Base64.base64ToByteArray(s);
  }

  @Test
  public void testMain() {
    // Test main method (console output not testable)
    String[] args = {};
    Base64.main(args);
  }
}
