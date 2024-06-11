import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        // Typical use case
        assertEquals("1111111111", Caverphone.caverphone(null));
        assertEquals("1111111111", Caverphone.caverphone(""));
        assertEquals("1111111111", Caverphone.caverphone(" "));

        // Edge case
        assertEquals("1111111111", Caverphone.caverphone("e"));

        // Error scenario
        assertEquals("2nark", Caverphone.caverphone("coughbark"));

        // Typical use case
        assertEquals("KT11111111", Caverphone.caverphone("kitten"));
        assertEquals("SN11111111", Caverphone.caverphone("sneezing"));

        // Edge case
        assertEquals("AT11111111", Caverphone.caverphone("a"));
        assertEquals("AT11111111", Caverphone.caverphone("at"));

        // Error scenario
        assertEquals("S3NK", Caverphone.caverphone("sn3k"));
    }

    @Test
    public void testEncode() throws EncoderException {
        // Typical use case
        assertEquals("KT11111111", Caverphone.encode("kitten"));

        // Edge case
        assertEquals("AT11111111", Caverphone.encode("a"));

        // Error scenario
        try {
            Caverphone.encode(null);
            fail("EncoderException expected");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        // Typical use case
        assertTrue(Caverphone.isCaverphoneEqual("kitten", "kitten"));

        // Edge case
        assertTrue(Caverphone.isCaverphoneEqual("", ""));

        // Error scenario
        assertFalse(Caverphone.isCaverphoneEqual("kitten", "sneezing"));
    }

}