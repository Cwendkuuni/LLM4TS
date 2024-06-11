import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("4vn1111111", caverphone.caverphone("4vn4nv4n"));
        assertEquals("222f", caverphone.caverphone("rrugh"));
        assertEquals("A111111111", caverphone.caverphone("enough"));
        assertEquals("cov357689", caverphone.caverphone("cough57js89r0"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("4vn1111111", caverphone.encode("4vn4nv4n"));
        assertEquals("222f", caverphone.encode("rrugh"));
        assertEquals("A111111111", caverphone.encode("enough"));
        assertEquals("cov357689", caverphone.encode("cough57js89r0"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        assertFalse(caverphone.isCaverphoneEqual("test1", "test2"));
    }

}