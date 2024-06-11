
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test with empty string
        String result1 = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result1);
        
        // Test with lowercase string
        String result2 = caverphone.caverphone("abcdef");
        Assert.assertEquals("1b2fcttm01", result2);
        
        // Test with uppercase string
        String result3 = caverphone.caverphone("ABCDEF");
        Assert.assertEquals("1b2fcttm01", result3);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        // Test with invalid object
        caverphone.encode(123);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test with lowercase string
        String result1 = caverphone.encode("abcdef");
        Assert.assertEquals("1b2fcttm01", result1);
        
        // Test with uppercase string
        String result2 = caverphone.encode("ABCDEF");
        Assert.assertEquals("1b2fcttm01", result2);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test with equal strings
        boolean result1 = caverphone.isCaverphoneEqual("abcdef", "abcdef");
        Assert.assertTrue(result1);
        
        // Test with different strings
        boolean result2 = caverphone.isCaverphoneEqual("abcdef", "abcdeg");
        Assert.assertFalse(result2);
    }

}
