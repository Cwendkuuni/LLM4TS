import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneBasicFunctionality() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("Expected caverphone code not generated", "HL11111111", result);
    }

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("Expected caverphone code not generated for empty string", "1111111111", result);
    }

    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("Expected caverphone code not generated for null string", "1111111111", result);
    }

    @Test
    public void testEncodeObjectInvalidType() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Integer(5));
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception, test passed
        }
    }

    @Test
    public void testEncodeStringFunctionality() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertEquals("Expected caverphone code not generated", "WRL11111111", result);
    }

    @Test
    public void testIsCaverphoneEqualTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue("Expected caverphones to be equal", result);
    }

    @Test
    public void testIsCaverphoneEqualFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse("Expected caverphones to be not equal", result);
    }

    // Additional edge cases and error scenarios can be added here

}