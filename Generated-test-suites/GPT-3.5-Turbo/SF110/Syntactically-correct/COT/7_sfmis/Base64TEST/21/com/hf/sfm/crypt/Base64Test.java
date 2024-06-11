
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Test with a valid altBase64 string
        String altBase64 = "SGVsbG8gd29ybGQ=";
        byte[] expected = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        byte[] result = Base64.altBase64ToByteArray(altBase64);
        Assert.assertArrayEquals(expected, result);
        
        // Test with an invalid altBase64 string that is not a multiple of four
        String invalidAltBase64 = "VGVzdGluZyB0aGluZ3MgdG8gYWRkIHRoaXM=";
        try {
            Base64.altBase64ToByteArray(invalidAltBase64);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Test with a valid base64 string
        String base64 = "U2Vjb25kIHdoZW4gdGhhdCBkYXRhIGluIFhNTCBvciBPcHRpb25zIQ==";
        byte[] expected = {83, 101, 99, 111, 110, 100, 32, 119, 104, 101, 110, 32, 100, 97, 116, 97, 32, 105, 110, 32, 88, 77, 76, 32, 111, 114, 32, 79, 112, 116, 105, 111, 110, 115, 33};
        byte[] result = Base64.base64ToByteArray(base64);
        Assert.assertArrayEquals(expected, result);
        
        // Test with an invalid base64 string that is not a multiple of four
        String invalidBase64 = "TGV0J3Mgbm90IGEgbXVsdGlwbGUgb2YgZGF0YSBvciBjaGFyYWN0ZXIgdGhhdCBpbiB4bWw";
        try {
            Base64.base64ToByteArray(invalidBase64);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Test with a valid byte array
        byte[] byteArray = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100};
        String expected = "SGVsbG8gd29ybGQ=";
        String result = Base64.byteArrayToAltBase64(byteArray);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Test with a valid byte array
        byte[] byteArray = {83, 101, 99, 111, 110, 100, 32, 119, 104, 101, 110, 32, 100, 97, 116, 97, 32, 105, 110, 32, 88, 77, 76, 32, 111, 114, 32, 79, 112, 116, 105, 111, 110, 115, 33};
        String expected = "U2Vjb25kIHdoZW4gdGhhdCBkYXRhIGluIFhNTCBvciBPcHRpb25zIQ==";
        String result = Base64.byteArrayToBase64(byteArray);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testMain() {
        // No need to test this method as it only contains a test case for the above methods
    }
}
