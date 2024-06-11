import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode(12345);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected Exception
        }
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("1221121111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }
}