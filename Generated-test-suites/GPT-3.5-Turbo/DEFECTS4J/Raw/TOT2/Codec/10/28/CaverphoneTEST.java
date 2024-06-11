import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("Expected result", result, "HOLO1111111");
    }

    @Test
    public void testCaverphoneWithNullInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("Expected result", result, "1111111111");
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertEquals("Expected result", result, "WALD1111111");
    }

    @Test
    public void testEncodeWithNonStringObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue("Successfully thrown EncoderException", true);
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hall");
        assertTrue("Expected result", result);
    }

}