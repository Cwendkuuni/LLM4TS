import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    private static Base64 base64;

    @BeforeClass
    public static void setUp() {
        base64 = new Base64();
    }

    // Test case for altBase64ToByteArray() - typical use case
    @Test
    public void testAltBase64ToByteArray_typical() {
        String s = "VGhpcyBpcyBhIHN0cmluZyB0ZXN0IHN0b3Jl";
        byte[] expected = "This is a string test store".getBytes();
        
        assertArrayEquals(expected, base64.altBase64ToByteArray(s));
    }

    // Test case for altBase64ToByteArray() - edge case
    @Test
    public void testAltBase64ToByteArray_edge() {
        String s = "AA==";
        byte[] expected = {0};
        
        assertArrayEquals(expected, base64.altBase64ToByteArray(s));
    }

    // Test case for altBase64ToByteArray() - error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_error() {
        String s = "InvalidBase64String";
        
        base64.altBase64ToByteArray(s);
    }

    // Test case for base64ToByteArray() - typical use case
    @Test
    public void testBase64ToByteArray_typical() {
        String s = "SGVsbG8gd29ybGQh";
        byte[] expected = "Hello world!".getBytes();
        
        assertArrayEquals(expected, base64.base64ToByteArray(s));
    }

    // Test case for base64ToByteArray() - edge case
    @Test
    public void testBase64ToByteArray_edge() {
        String s = "AAA=";
        byte[] expected = {0};
        
        assertArrayEquals(expected, base64.base64ToByteArray(s));
    }

    // Test case for base64ToByteArray() - error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_error() {
        String s = "InvalidBase64String";
        
        base64.base64ToByteArray(s);
    }

    // Test case for byteArrayToAltBase64() - typical use case
    @Test
    public void testByteArrayToAltBase64_typical() {
        byte[] bb = "This is a string test store".getBytes();
        String expected = "VGhpcyBpcyBhIHN0cmluZyB0ZXN0IHN0b3Jl";
        
        assertEquals(expected, base64.byteArrayToAltBase64(bb));
    }

    // Test case for byteArrayToAltBase64() - edge case
    @Test
    public void testByteArrayToAltBase64_edge() {
        byte[] bb = {0};
        String expected = "AA==";
        
        assertEquals(expected, base64.byteArrayToAltBase64(bb));
    }

    // Test case for byteArrayToBase64() - typical use case
    @Test
    public void testByteArrayToBase64_typical() {
        byte[] bb = "Hello world!".getBytes();
        String expected = "SGVsbG8gd29ybGQh";
        
        assertEquals(expected, base64.byteArrayToBase64(bb));
    }

    // Test case for byteArrayToBase64() - edge case
    @Test
    public void testByteArrayToBase64_edge() {
        byte[] bb = {0};
        String expected = "AAA=";
        
        assertEquals(expected, base64.byteArrayToBase64(bb));
    }

    // Test case for main() method
    @Test
    public void testMain() {
        // Assuming that the main() method prints the output to console,
        // we cannot directly test the output. This test case is present 
        // to ensure that the method runs without any exception.
        String[] args = {};
        
        base64.main(args);
    }
}