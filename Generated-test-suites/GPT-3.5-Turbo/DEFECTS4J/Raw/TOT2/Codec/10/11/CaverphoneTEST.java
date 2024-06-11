import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    // Test case for caverphone method with typical use case
    @Test
    public void testCaverphoneTypical() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("example");
        assertEquals("e2511m1111", result);
    }

    // Test case for caverphone method with edge case of empty input
    @Test
    public void testCaverphoneEmptyInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    // Test case for caverphone method with edge case of all special characters
    @Test
    public void testCaverphoneSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("!@#$%^&*()");
        assertEquals("1111111111", result);
    }

    // Test case for caverphone method with exception handling scenario
    @Test(expected = NullPointerException.class)
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    // Test case for encode method with typical use case
    @Test
    public void testEncodeTypical() {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode("example");
        assertEquals("e2511m1111", result);
    }

    // Test case for encode method with edge case of non-string object
    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    // Test case for isCaverphoneEqual method with typical use case
    @Test
    public void testIsCaverphoneEqualTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }

    // Test case for isCaverphoneEqual method with typical use case of different caverphones
    @Test
    public void testIsCaverphoneEqualFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", "examples");
        assertFalse(result);
    }
}