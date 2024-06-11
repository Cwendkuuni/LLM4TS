import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Edge case: empty string
        // Expected output: "1111111111"
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        
        // Basic test case: lowercase string
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.caverphone("kumar"));
        
        // Basic test case: uppercase string
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.caverphone("KUMAR"));
        
        // Edge case: string with non-alphabetic characters
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.caverphone("_k_u-m*a@r"));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Edge case: empty string
        // Expected output: "1111111111"
        Assert.assertEquals("1111111111", caverphone.encode(""));
        
        // Basic test case: lowercase string
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.encode("kumar"));
        
        // Basic test case: uppercase string
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.encode("KUMAR"));
        
        // Edge case: string with non-alphabetic characters
        // Expected output: "KMA1111111"
        Assert.assertEquals("KMA1111111", caverphone.encode("_k_u-m*a@r"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case: equal strings
        // Expected output: true
        Assert.assertTrue(caverphone.isCaverphoneEqual("kumar", "kumar"));
        
        // Basic test case: different strings
        // Expected output: false
        Assert.assertFalse(caverphone.isCaverphoneEqual("kumar", "smith"));
    }
}