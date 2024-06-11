import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {
    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone("hello");
        assertEquals("hla1111111", result);
    }

    @Test
    public void testEncode() {
        Object result = caverphone.encode("hello");
        assertEquals("hla1111111", result);
    }

    @Test
    public void testEncodeInvalidObject() {
        try {
            Object result = caverphone.encode(12345);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncodeString() {
        String result = caverphone.encode("hello");
        assertEquals("hla1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hola");
        assertEquals(true, result);
    }
}