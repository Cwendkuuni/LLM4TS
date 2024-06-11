import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    // Test for the caverphone method
    @Test
    public void testCaverphone() {
        // Typical use case
        String result1 = caverphone.caverphone("example");
        assertEquals("ekssampel1111", result1);

        // Edge case - empty string
        String result2 = caverphone.caverphone("");
        assertEquals("1111111111", result2);

        // Error scenario - null string
        String result3 = caverphone.caverphone(null);
        assertNull(result3);
    }

    // Test for the encode method
    @Test
    public void testEncode() throws EncoderException {
        // Typical use case
        Object result1 = caverphone.encode("example");
        assertEquals("ekssampel1111", (String) result1);

        // Edge case - empty string
        Object result2 = caverphone.encode("");
        assertEquals("1111111111", (String) result2);

        // Error scenario - invalid object type
        try {
            Object result3 = caverphone.encode(12345);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    // Test for the isCaverphoneEqual method
    @Test
    public void testIsCaverphoneEqual() {
        // Typical use case - caverphones are equal
        boolean result1 = caverphone.isCaverphoneEqual("example", "ekssampel");
        assertTrue(result1);

        // Typical use case - caverphones are not equal
        boolean result2 = caverphone.isCaverphoneEqual("example", "different");
        assertFalse(result2);
    }

}