import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone_basicInput() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("y111111111", caverphone.caverphone("y"));
        assertEquals("A111111111", caverphone.caverphone("aeiou"));
    }

    @Test
    public void testEncode_basicInput() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("y111111111", caverphone.encode("y"));
        assertEquals("A111111111", caverphone.encode("aeiou"));
    }

    @Test
    public void testIsCaverphoneEqual_identicalInput() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

    // Add additional test cases for edge cases, exceptions handling, etc.

}