
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);

        // Test lowercase string
        result = caverphone.caverphone("hello");
        assertEquals("HL11111111", result);

        // Test uppercase string
        result = caverphone.caverphone("WORLD");
        assertEquals("WR11111111", result);

        // Test string with special characters
        result = caverphone.caverphone("123!@#$%");
        assertEquals("1111111111", result);

        // Test string with Caverphone algorithm replacements
        result = caverphone.caverphone("replace");
        assertEquals("BL1S111111", result); 
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectInvalidType() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testEncodeObjectValidType() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode("hello");
        assertEquals("HL11111111", result);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertEquals("WR11111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test equal strings
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue(result);

        // Test unequal strings
        result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

}
