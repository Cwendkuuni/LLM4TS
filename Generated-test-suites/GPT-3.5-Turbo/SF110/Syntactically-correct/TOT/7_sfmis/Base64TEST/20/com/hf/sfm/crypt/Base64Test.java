import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Before
    public void setUp() {
        // Set up any necessary preparation for the tests
    }

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAltBase64ToByteArray_invalidInput() {
        String input = "SGVsbG8gd29ybGQ";
        try {
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray_validInput() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray_invalidInput() {
        String input = "SGVsbG8gd29ybGQ";
        try {
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

}