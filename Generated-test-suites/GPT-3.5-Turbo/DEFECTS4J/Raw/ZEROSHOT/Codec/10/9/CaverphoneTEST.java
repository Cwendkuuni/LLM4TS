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
        assertEquals("cvfk", caverphone.caverphone("Caverphone"));
        assertEquals("txt", caverphone.caverphone("Txt"));
        assertEquals("Jvn", caverphone.caverphone("Java"));
        assertEquals("1111111111", caverphone.caverphone("12345"));
    }

    @Test
    public void testEncode() {
        assertEquals("cvfk", caverphone.encode(null));
        assertEquals("cvfk", caverphone.encode(""));
        assertEquals("cvfk", caverphone.encode("Caverphone"));
        assertEquals("txt", caverphone.encode("Txt"));
        assertEquals("Jvn", caverphone.encode("Java"));
        assertEquals("cvfk", caverphone.encode("12345"));
        assertTrue(caverphone.encode("Caverphone").equals(caverphone.encode("CAVERPHONE")));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Caverphone", "CAVERPHONE"));
        assertFalse(caverphone.isCaverphoneEqual("Java", "Txt"));
    }

}