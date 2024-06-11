import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        Assert.assertEquals("tst1111111", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode("test");
        Assert.assertEquals("tst1111111", result);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        Assert.assertEquals("tst1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "tst");
        Assert.assertTrue(result);
    }
}