import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Basic test case
        assertEquals("1111111111", caverphone.caverphone(null));

        // Edge cases and exceptions
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("ro2f111111", caverphone.caverphone("rough"));
        assertEquals("tou2f111111", caverphone.caverphone("tough"));
        assertEquals("A111111111", caverphone.caverphone("a"));
        assertEquals("A111111111", caverphone.caverphone("A"));
        assertEquals("karker", caverphone.caverphone("crcr"));
        assertEquals("s2t2", caverphone.caverphone("shth"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("karker", caverphone.encode("crcr"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("crcr", "ccccrrrr"));
        assertFalse(caverphone.isCaverphoneEqual("crcr", "crcrcrcr"));
    }
}