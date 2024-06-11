import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }
  
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }
  
    @Test
    public void testByteArrayToBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }
  
    @Test
    public void testMain() {
        // Need to test the main method by redirecting System.out and capturing the output
    }
}