import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("  "));

        assertEquals("T2222A", caverphone.caverphone("tough"));
        assertEquals("K2ff3r3", caverphone.caverphone("crafter"));
        assertEquals("H3l3", caverphone.caverphone("hello"));
    }

    @Test
    public void testEncode() {
        assertEquals("T2222A", caverphone.encode("tough"));
        assertEquals("K2ff3r3", caverphone.encode("crafter"));
        assertEquals("H3l3", caverphone.encode("hello"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "mellow"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

}