
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.*;

public class CaverphoneTest {
    Caverphone encoder;

    @Before
    public void setUp() {
        encoder = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", encoder.caverphone(null));
        assertEquals("1111111111", encoder.caverphone(""));
        assertEquals("1111", encoder.caverphone("abc"));
        // Add more basic test cases here

    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringParameter() throws EncoderException {
        encoder.encode(123);
    }

    @Test
    public void testEncode() {
        assertEquals("1111111111", encoder.encode(null));
        assertEquals("1111111111", encoder.encode(""));
        assertEquals("1111", encoder.encode("abc"));
        // Add more encode test cases here

    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(encoder.isCaverphoneEqual(null, null));
        assertTrue(encoder.isCaverphoneEqual("", ""));
        assertTrue(encoder.isCaverphoneEqual("abc", "abc"));
        assertFalse(encoder.isCaverphoneEqual("abc", "def"));
        // Add more isCaverphoneEqual test cases here

    }
}
