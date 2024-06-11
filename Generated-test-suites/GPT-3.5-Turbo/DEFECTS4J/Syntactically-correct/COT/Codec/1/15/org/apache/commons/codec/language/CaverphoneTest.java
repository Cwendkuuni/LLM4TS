
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    // Test the caverphone method
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test null input
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        
        // Test empty input
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test lowercase input
        Assert.assertEquals("r111132111", caverphone.caverphone("Robert"));
        
        // Test uppercase input
        Assert.assertEquals("r111132111", caverphone.caverphone("ROBERT"));
        
        // Test input with non-alphabetic characters
        Assert.assertEquals("c111111111", caverphone.caverphone("c123!@#$%^&*()"));
        
        // Test edge case of final e removal  
        Assert.assertEquals("a11111111", caverphone.caverphone("cate"));
    }
    
    // Test the encode method
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test null input
        try {
            caverphone.encode(null);
            Assert.fail("EncoderException not thrown for null input");
        } catch (EncoderException e) {
            // Expected exception
        }
        
        // Test valid input
        Assert.assertEquals("r111132111", caverphone.encode("Robert"));
        
        // Test edge case of final e removal  
        Assert.assertEquals("a11111111", caverphone.encode("cate"));
    }
    
    // Test the isCaverphoneEqual method
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equal caverphones
        Assert.assertTrue(caverphone.isCaverphoneEqual("Robert", "ROBERT"));
        
        // Test different caverphones
        Assert.assertFalse(caverphone.isCaverphoneEqual("Robert", "Alice"));
    }

}