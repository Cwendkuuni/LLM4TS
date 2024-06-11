import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "tst");
        assertTrue(result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

}