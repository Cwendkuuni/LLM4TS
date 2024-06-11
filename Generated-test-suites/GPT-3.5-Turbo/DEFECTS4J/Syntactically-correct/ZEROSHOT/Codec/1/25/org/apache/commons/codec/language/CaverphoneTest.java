import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone("test");
        assertEquals("test", result);
    }

    @Test
    public void testCaverphoneWithNull() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithEmptyString() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Object result = caverphone.encode("test");
        assertEquals("test", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() throws EncoderException {
        Object result = caverphone.encode(12345);
    }

    @Test
    public void testEncodeString() {
        String result = caverphone.encode("test");
        assertEquals("test", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }
}