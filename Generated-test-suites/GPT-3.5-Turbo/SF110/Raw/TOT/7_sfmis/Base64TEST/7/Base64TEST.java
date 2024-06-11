import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    // altBase64ToByteArray method test cases
    @Test
    public void testAltBase64ToByteArray_typicalCase() {
        String input = "TWFu";
        byte[] expectedOutput = {77, 97, 110};
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        String input = "TWF=";
        Base64.altBase64ToByteArray(input);
    }
    
    // base64ToByteArray method test cases
    @Test
    public void testBase64ToByteArray_typicalCase() {
        String input = "TWFu";
        byte[] expectedOutput = {77, 97, 110};
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(input));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        String input = "TWF=";
        Base64.base64ToByteArray(input);
    }
    
    // byteArrayToAltBase64 method test case
    @Test
    public void testByteArrayToAltBase64_typicalCase() {
        byte[] input = {77, 97, 110};
        String expectedOutput = "TWFu";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(input));
    }
    
    // byteArrayToBase64 method test case
    @Test
    public void testByteArrayToBase64_typicalCase() {
        byte[] input = {77, 97, 110};
        String expectedOutput = "TWFu";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(input));
    }

}