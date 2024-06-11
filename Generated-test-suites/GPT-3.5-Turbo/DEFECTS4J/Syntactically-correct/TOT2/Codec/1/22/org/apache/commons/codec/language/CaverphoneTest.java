import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    // Test case for caverphone() method
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone("123"));
        
        // Edge cases
        assertEquals("AX33K6111F", caverphone.caverphone("testing"));
        assertEquals("K3111611F3Q", caverphone.caverphone("hello123"));
        
        // Exception handling
        // No specific exception to test for this method
    }

    // Test case for encode(Object) method
    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        try {
            assertEquals("1111111111", caverphone.encode("test"));
        } catch (EncoderException e) {
            fail("Exception should not be thrown");
        }
        
        // Edge cases
        try {
            assertEquals("AX33K6111F", caverphone.encode("testing"));
        } catch (EncoderException e) {
            fail("Exception should not be thrown");
        }

        // Exception handling
        assertThrows(EncoderException.class, () -> {
            caverphone.encode(123);
        });
    }

    // Test case for encode(String) method
    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.encode("test"));
        
        // Edge cases
        assertEquals("AX33K6111F", caverphone.encode("testing"));
    }

    // Test case for isCaverphoneEqual() method
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        assertFalse(caverphone.isCaverphoneEqual("test", "hello"));
        
        // Edge cases
        assertTrue(caverphone.isCaverphoneEqual("testing", "TeStInG"));
    }
}