import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("12345"));
        assertEquals("TKTWTS1T1", caverphone.caverphone("testing"));
        assertEquals("AL23L3A111", caverphone.caverphone("algorithm"));
        assertEquals("AAP1A11111", caverphone.caverphone("apache"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("TKTWTS1T1", caverphone.encode("testing"));
        assertEquals("AL23L3A111", caverphone.encode("algorithm"));
        assertEquals("AAP1A11111", caverphone.encode("apache"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("testing", "testing"));
        assertFalse(caverphone.isCaverphoneEqual("testing", "algorithm"));
    }
}