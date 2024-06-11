import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        String result = caverphone.caverphone("hello");
        Assert.assertEquals("HA11111111", result);
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();

        try {
            Object result = caverphone.encode("world");
            Assert.assertEquals("WA11111111", result);
        } catch (EncoderException ex) {
            Assert.fail("Exception not expected in this scenario");
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode("test");
        Assert.assertEquals("TST1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        boolean result = caverphone.isCaverphoneEqual("cat", "kat");
        Assert.assertTrue(result);
    }

    // Edge cases
    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();

        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();

        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        Assert.assertFalse(result);
    }

    // Exception handling
    @Test
    public void testEncodeObjectWithInvalidType() {
        Caverphone caverphone = new Caverphone();

        try {
            Object result = caverphone.encode(123);
            Assert.fail("Exception expected in this scenario");
        } catch (EncoderException ex) {
            // Exception expected
        }
    }
}