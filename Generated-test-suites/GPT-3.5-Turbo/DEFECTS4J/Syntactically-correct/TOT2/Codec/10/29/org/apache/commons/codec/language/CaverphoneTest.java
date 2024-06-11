import static org.junit.Assert.*;

import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("A111111111", caverphone.caverphone("1234"));
        assertEquals("1111111111", caverphone.caverphone("!@#$"));
        assertEquals("TRF", caverphone.caverphone("tough rough enough"));
    }

    @Test
    public void testEncodeObject() {
        try {
            caverphone.encode(123);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncodeString() {
        assertEquals("A111111111", caverphone.encode("1234"));
        assertEquals("TRF", caverphone.encode("tough rough enough"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertTrue(caverphone.isCaverphoneEqual("world", "world"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}