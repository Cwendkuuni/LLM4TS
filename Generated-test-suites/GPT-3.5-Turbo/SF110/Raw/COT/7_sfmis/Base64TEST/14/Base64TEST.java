import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    // Test case for altBase64ToByteArray method
    @Test
    public void testAltBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expected = "Hello world!".getBytes();
        byte[] result = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    // Test case for base64ToByteArray method
    @Test
    public void testBase64ToByteArray() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expected = "Hello world!".getBytes();
        byte[] result = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, result);
    }

    // Test case for byteArrayToAltBase64 method
    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = "Hello world!".getBytes();
        String expected = "SGVsbG8gd29ybGQh";
        String result = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, result);
    }

    // Test case for byteArrayToBase64 method
    @Test
    public void testByteArrayToBase64() {
        byte[] b = "Hello world!".getBytes();
        String expected = "SGVsbG8gd29ybGQh";
        String result = Base64.byteArrayToBase64(b);
        assertEquals(expected, result);
    }
    
    // Test case for _$23183 method when IllegalArgumentException is expected
    @Test(expected = IllegalArgumentException.class)
    public void test_$23183_IllegalArgumentException() {
        byte[] bb = Base64._$23169;
        char c = '!';
        Base64._$23183(c, bb);
    }

    // Test case for main method
    @Test
    public void testMain() {
        // Not testing main method as it only contains print statements
        assertTrue(true);
    }

}