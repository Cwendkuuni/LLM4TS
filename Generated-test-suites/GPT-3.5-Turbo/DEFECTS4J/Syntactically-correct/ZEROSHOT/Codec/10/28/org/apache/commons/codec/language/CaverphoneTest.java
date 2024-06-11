import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_emptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_nullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_validString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("apache");
        assertEquals("SPK1111111", result);
    }

    @Test
    public void testEncode_objectTypeNotString() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_validString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("apache");
        assertEquals("SPK1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("apache", "apache");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("apache", "commons");
        assertFalse(result);
    }

}