
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "aGVsbG8gd29ybGQ="; // "hello world"
        byte[] expected = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "aGVsbG8gd29ybGQ="; // "hello world"
        byte[] expected = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // "hello world"
        String expected = "aGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100}; // "hello world"
        String expected = "aGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMain() {
        // Test the main method by redirecting System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        Base64.main(new String[]{});
        String expected = "aGVsbG8gd29ybGQ=\nhello world\n";
        String result = outContent.toString();
        assertEquals(expected, result);
        
        // Reset System.out
        System.setOut(System.out);
    }
}
