import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);

        result = caverphone.caverphone(null);
        assertEquals("1111111111", result);

        result = caverphone.caverphone("Hello");
        assertEquals("H2l", result);

        result = caverphone.caverphone("World");
        assertEquals("Wlh2", result);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Object result = caverphone.encode("Hello");
        assertEquals("H2l", result);

        result = caverphone.encode(null);
        assertEquals("1111111111", result);

        try {
            result = caverphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodeString() {
        String result = caverphone.encode("Hello");
        assertEquals("H2l", result);

        result = caverphone.encode(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("Hello", "H2l");
        assertTrue(result);

        result = caverphone.isCaverphoneEqual("Hello", "World");
        assertFalse(result);
    }

}