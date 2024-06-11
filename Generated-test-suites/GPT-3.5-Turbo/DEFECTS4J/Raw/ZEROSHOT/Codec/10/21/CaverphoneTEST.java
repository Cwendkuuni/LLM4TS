import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        String actual = caverphone.caverphone("");
        String expected = "1111111111";
        assertEquals(expected, actual);

        // Test various input strings
        actual = caverphone.caverphone("cough");
        expected = "COF2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("rough");
        expected = "ROF2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("tough");
        expected = "TOF2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("enough");
        expected = "ENO2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("trough");
        expected = "TRF2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("gnash");
        expected = "KNASH";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("embark");
        expected = "EMB2K";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("cycling");
        expected = "SYKL2N";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("healthy");
        expected = "HAL2";
        assertEquals(expected, actual);

        actual = caverphone.caverphone("rhythm");
        expected = "2YM";
        assertEquals(expected, actual);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        String actual = caverphone.encode("");
        String expected = "1111111111";
        assertEquals(expected, actual);

        // Test non-string object
        try {
            caverphone.encode(123);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Exception expected
        }

        // Test string object
        actual = caverphone.encode("cough");
        expected = "COF2";
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test equal caverphones
        boolean actual = caverphone.isCaverphoneEqual("cough", "cou2f");
        assertTrue(actual);

        // Test different caverphones
        actual = caverphone.isCaverphoneEqual("cough", "ruff");
        assertFalse(actual);
    }
}