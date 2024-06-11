import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("A11111", caverphone.caverphone("123abc456")); // edge case with numbers and special characters
        assertEquals("tou2f", caverphone.caverphone("tough")); // typical use case
        assertEquals("klh32s321411111111", caverphone.caverphone("kaleidoscope")); // boundary case
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();

        try {
            caverphone.encode(new Integer(5));
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.encode("")); // edge case
        assertEquals("tou2f", caverphone.encode("tough")); // typical use case
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC")); // typical use case, ignoring case
        assertFalse(caverphone.isCaverphoneEqual("hello", "world")); // typical use case, not equal
    }
}