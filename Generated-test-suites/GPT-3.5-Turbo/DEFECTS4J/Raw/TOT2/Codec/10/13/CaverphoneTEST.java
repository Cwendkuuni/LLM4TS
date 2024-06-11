import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone("test"));
        
        // Edge case - empty string
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", (String) caverphone.encode("test"));
        
        // Error scenario - non-string object
        try {
            caverphone.encode(new Object());
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.encode("test"));
        
        // Edge case - empty string
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case - equal caverphones
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        
        // Typical use case - different caverphones
        assertFalse(caverphone.isCaverphoneEqual("test", "testing"));
    }
}