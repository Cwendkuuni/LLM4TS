import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone("foobar");
        assertEquals("should return the expected caverphone value", "1326231321", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Object result = caverphone.encode("foobar");
        assertEquals("should return the expected caverphone value", "1326231321", result);
    }

    @Test
    public void testEncodeInvalidObject() throws EncoderException {
        try {
            Object result = caverphone.encode(123);
        } catch (EncoderException e) {
            assertEquals("should throw EncoderException with correct message", "Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncodeString() {
        String result = caverphone.encode("foobar");
        assertEquals("should return the expected caverphone value", "1326231321", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("foobar", "foobaz");
        assertFalse("should return false if the caverphones are not equal", result);

        result = caverphone.isCaverphoneEqual("foobar", "foobar");
        assertTrue("should return true if the caverphones are equal", result);
    }

}