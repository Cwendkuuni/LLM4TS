import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        Assert.assertEquals("1234567891", caverphone.caverphone("hello"));
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        
        // Edge cases
        Assert.assertEquals("6661111111", caverphone.caverphone("000000"));
        Assert.assertEquals("1111111111", caverphone.caverphone("Sdjkfkj%-#@$#"));
        
        // Error scenarios
        // Since there are no specific error scenarios mentioned in the requirements,
        // we assume that passing non-alphabetic characters or special characters should return the same result as passing an empty string or null.
        Assert.assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        Assert.assertEquals("1234567891", caverphone.encode("hello"));
        Assert.assertEquals("1111111111", caverphone.encode(""));
        Assert.assertEquals("1111111111", caverphone.encode(null));
        
        // Edge cases
        Assert.assertEquals("6661111111", caverphone.encode("000000"));
        Assert.assertEquals("1111111111", caverphone.encode("Sdjkfkj%-#@$#"));
        
        // Error scenarios
        // Since there are no specific error scenarios mentioned in the requirements,
        // we assume that passing non-alphabetic characters or special characters should return the same result as passing an empty string or null.
        Assert.assertEquals("1111111111", caverphone.encode("!@#$%^&*()"));
        
        // Exception scenario
        try {
            caverphone.encode(123);
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        Assert.assertTrue(caverphone.isCaverphoneEqual("hello", "HELLO"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("coding", "CODE"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("test", "testing"));
        
        // Edge cases
        Assert.assertFalse(caverphone.isCaverphoneEqual("1234567891", "123456"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("", ""));
        
        // Error scenarios
        Assert.assertTrue(caverphone.isCaverphoneEqual(null, null));
    }
}