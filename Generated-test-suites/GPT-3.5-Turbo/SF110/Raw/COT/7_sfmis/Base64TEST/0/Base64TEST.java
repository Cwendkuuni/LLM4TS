import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {1, 2, 3};
        byte[] actual = Base64.altBase64ToByteArray("AQID");
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {1, 2, 3};
        byte[] actual = Base64.base64ToByteArray("AQID");
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] byteArray = {1, 2, 3};
        String expected = "AQID";
        String actual = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] byteArray = {1, 2, 3};
        String expected = "AQID";
        String actual = Base64.byteArrayToBase64(byteArray);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMain() {
        // Cannot fully test main method as it relies on console input and output
        // Perform manual testing instead
    }

    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidString() {
        Base64.altBase64ToByteArray("ABC"); // String length is not a multiple of four
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidString() {
        Base64.base64ToByteArray("ABC"); // String length is not a multiple of four
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToAltBase64EmptyArray() {
        byte[] byteArray = {};
        Base64.byteArrayToAltBase64(byteArray); // Empty array
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayToBase64EmptyArray() {
        byte[] byteArray = {};
        Base64.byteArrayToBase64(byteArray); // Empty array
    }
}