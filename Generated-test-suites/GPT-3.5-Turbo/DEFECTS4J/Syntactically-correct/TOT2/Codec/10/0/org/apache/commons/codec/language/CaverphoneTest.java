import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("AT11111111", caverphone.caverphone("a test"));
        assertEquals("TK31311111", caverphone.caverphone("The quick brown fox"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode("example");
            assertTrue(result instanceof String);
            assertEquals("AX1", result);
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown for a valid String");
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("AX1", caverphone.encode("example"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("test", "test"));
        assertFalse(caverphone.isCaverphoneEqual("test", "different"));
    }

}