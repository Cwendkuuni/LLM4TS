import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("", caverphone.caverphone(""));
        assertEquals("A111A11111", caverphone.caverphone("Test String!"));
        assertEquals("AK111A1111", caverphone.caverphone("Another Test"));
        assertEquals("K111A11113", caverphone.caverphone("12345"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("", caverphone.encode(""));
        assertEquals("A111A11111", caverphone.encode("Test String!"));
        assertEquals("AK111A1111", caverphone.encode("Another Test"));
        assertEquals("K111A11113", caverphone.encode("12345"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("Test", "Test"));
        assertFalse(caverphone.isCaverphoneEqual("Test", "Test1"));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertFalse(caverphone.isCaverphoneEqual("Test1", "Test2"));
    }
}