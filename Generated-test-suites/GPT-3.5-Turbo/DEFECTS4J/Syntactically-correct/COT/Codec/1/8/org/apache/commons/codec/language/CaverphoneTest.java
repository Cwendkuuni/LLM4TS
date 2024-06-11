
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("kmpt", caverphone.caverphone("kmpt"));
        assertEquals("kkkkk", caverphone.caverphone("ckkkkey"));
        assertEquals("fsWnM", caverphone.caverphone("unicode"));
        assertEquals("WnM", caverphone.caverphone("quite"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("kmpt", caverphone.encode("kmpt"));
        assertEquals("kkkkk", caverphone.encode("ckkkkey"));
        assertEquals("fsWnM", caverphone.encode("unicode"));
        assertEquals("WnM", caverphone.encode("quite"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("kmpt", "kmpt"));
        assertFalse(caverphone.isCaverphoneEqual("kmpt", "caverphone"));
    }
}

