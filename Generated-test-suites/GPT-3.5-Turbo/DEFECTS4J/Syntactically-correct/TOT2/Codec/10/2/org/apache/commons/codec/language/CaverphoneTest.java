import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("S1111A111A1", caverphone.caverphone("StackOverflow"));
    }

    @Test
    public void testEncodeObject() {
        try {
            assertEquals("S1111A111A1", caverphone.encode("StackOverflow"));
        } catch (EncoderException e) {
            fail("Should not throw EncoderException");
        }
        
        try {
            caverphone.encode(new Object());
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        assertEquals("S1111A111A1", caverphone.encode("StackOverflow"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

}