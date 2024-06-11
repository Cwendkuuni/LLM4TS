import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneBasic() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("K11111111", caverphone.caverphone("K"));
        assertEquals("3111111111", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneEdgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("C11111111", caverphone.caverphone("cough"));
        assertEquals("22gh22", caverphone.caverphone("gh"));
        assertEquals("Wi2", caverphone.caverphone("why"));
    }

    @Test
    public void testCaverphoneExceptions() {
        Caverphone caverphone = new Caverphone();
        assertThrows(NullPointerException.class, () -> caverphone.caverphone(null));
    }

    @Test
    public void testEncodeBasic() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("K11111111", caverphone.encode("K"));
        assertEquals("3111111111", caverphone.encode("3"));
    }

    @Test
    public void testEncodeException() {
        Caverphone caverphone = new Caverphone();
        assertThrows(EncoderException.class, () -> caverphone.encode(123));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

}