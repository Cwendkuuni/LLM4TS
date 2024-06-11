import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        Assert.assertEquals("1111111111", caverphone.caverphone(""));

        // Test lowercase string
        Assert.assertEquals("kslxfrs2", caverphone.caverphone("kslxfRS"));
        
        // Test string with special characters
        Assert.assertEquals("23sgwklmpnft", caverphone.caverphone("23sgwklmpnFT#"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();

        // Test non-string object
        try {
            caverphone.encode(123);
            Assert.fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Exception expected
        }

        // Test string object
        Assert.assertEquals("kslxfRS", caverphone.encode("kslxfRS"));
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();

        // Test lowercase string
        Assert.assertEquals("kslxfrs2", caverphone.encode("kslxfRS"));

        // Test empty string
        Assert.assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test two identical strings
        Assert.assertTrue(caverphone.isCaverphoneEqual("kslxfRS", "kslxfRS"));

        // Test two different strings
        Assert.assertFalse(caverphone.isCaverphoneEqual("kslxfRS", "abc123"));
    }
}