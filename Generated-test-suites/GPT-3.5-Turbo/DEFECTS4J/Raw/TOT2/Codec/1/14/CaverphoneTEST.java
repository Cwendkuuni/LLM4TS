import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphoneBasicFunctionality() {
        String result = caverphone.caverphone("test");
        assertEquals("t1111111111", result);
    }

    @Test
    public void testCaverphoneEmptyInput() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneWithSpecialCharacters() {
        String result = caverphone.caverphone("&$#@test!");
        assertEquals("t1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqualTrue() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqualFalse() {
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

    @Test
    public void testEncodeObjectThrowsEncoderException() {
        try {
            caverphone.encode(new Integer(123));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        String result = caverphone.encode("test");
        assertEquals("t1111111111", result);
    }
}