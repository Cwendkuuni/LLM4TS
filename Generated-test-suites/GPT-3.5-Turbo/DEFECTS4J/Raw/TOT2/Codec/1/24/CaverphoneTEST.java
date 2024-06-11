import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        assertEquals("1111111111", caverphone.caverphone("123"));
        assertEquals("K111111111", caverphone.caverphone("abc"));
        
        assertEquals("K111111111", caverphone.caverphone("ABC"));
        assertEquals("1111111111", caverphone.caverphone(" "));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        
        assertEquals("1111111111", caverphone.encode("abc"));
        assertEquals("K111111111", caverphone.encode("ABC"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "123"));
    }

}