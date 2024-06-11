import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void caverphone_NullString_ReturnsExpectedOutput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void caverphone_EmptyString_ReturnsExpectedOutput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void caverphone_ValidString_ReturnsExpectedOutput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void encode_ObjectNotString_ThrowsEncoderException() {
        Caverphone caverphone = new Caverphone();
        Object object = new Object();
        try {
            caverphone.encode(object);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void encode_ValidString_ReturnsExpectedOutput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("tst1111111", result);
    }

    @Test
    public void isCaverphoneEqual_IdenticalStrings_ReturnsTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void isCaverphoneEqual_DifferentStrings_ReturnsFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "abcd");
        assertFalse(result);
    }

}