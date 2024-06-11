import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("test");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode("test");
            assertTrue(result instanceof String);
            assertEquals("1111111111", result);
        } catch (EncoderException e) {
            fail("Encoding threw an exception");
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        assertEquals("1111111111", result);
    }

    @Test
    public void testisCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test1", "test2");
        assertFalse(result);
    }

}