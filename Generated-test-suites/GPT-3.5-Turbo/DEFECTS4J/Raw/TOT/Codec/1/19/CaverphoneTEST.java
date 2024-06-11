import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        Assert.assertEquals("1111111111", caverphone.caverphone(""));

        // Edge cases
        Assert.assertEquals("kb", caverphone.caverphone("ABC"));
        Assert.assertEquals("sy3N", caverphone.caverphone("SyNth3s3s"));
        Assert.assertEquals("Akb", caverphone.caverphone("CkB"));

        // Error scenarios
        Assert.assertEquals("kb", caverphone.caverphone("123"));
        Assert.assertEquals("kb", caverphone.caverphone("!@#$%^&*()"));
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        Assert.assertEquals("1111111111", caverphone.encode((Object) null));
        Assert.assertEquals("1111111111", caverphone.encode((Object) ""));

        // Error scenarios
        try {
            caverphone.encode((Object) 123);
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }

        try {
            caverphone.encode((Object) "!@#$%^&*()");
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        Assert.assertEquals("1111111111", caverphone.encode(null));
        Assert.assertEquals("1111111111", caverphone.encode(""));

        // Error scenarios
        Assert.assertEquals("kb", caverphone.encode("123"));
        Assert.assertEquals("kb", caverphone.encode("!@#$%^&*()"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        Assert.assertTrue(caverphone.isCaverphoneEqual(null, null));
        Assert.assertTrue(caverphone.isCaverphoneEqual("", ""));

        // Edge cases
        Assert.assertTrue(caverphone.isCaverphoneEqual("abc", "ABC"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("SyNth3s3s", "sy3Nth3s3s"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("CkB", "CKB"));

        // Negative cases
        Assert.assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("abc", "ABC"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("SyNth3s3s", "SyNth3s3"));
    }

}