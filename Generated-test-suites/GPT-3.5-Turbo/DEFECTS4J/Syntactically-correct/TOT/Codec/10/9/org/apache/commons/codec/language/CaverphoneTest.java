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
    public void testCaverphone_NullInput_Returns1111111111() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_EmptyInput_Returns1111111111() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_TypicalInput_ReturnsExpectedResult() {
        String result = caverphone.caverphone("example");
        assertEquals("AMPL111111", result);
    }

    @Test
    public void testCaverphone_InputWithWhiteSpaceAndSpecialCharacters_ReturnsExpectedResult() {
        String result = caverphone.caverphone("e$xampl12e");
        assertEquals("AMPL111111", result);
    }

    @Test
    public void testCaverphone_StringWithRepeatedCharacters_ReturnsExpectedResult() {
        String result = caverphone.caverphone("aabbcc");
        assertEquals("ABBCC11111", result);
    }

    @Test
    public void testEncode_NullInput_ThrowsEncoderException() {
        try {
            caverphone.encode(null);
            fail("Expected EncoderException was not thrown.");
        } catch (EncoderException e) {
            // Test passed
        }
    }

    @Test
    public void testEncode_NonStringInput_ThrowsEncoderException() {
        try {
            caverphone.encode(123);
            fail("Expected EncoderException was not thrown.");
        } catch (EncoderException e) {
            // Test passed
        }
    }

    @Test
    public void testEncode_TypicalStringInput_ReturnsExpectedResult() {
        String result = caverphone.encode("example");
        assertEquals("AMPL111111", result);
    }

    @Test
    public void testEncode_InputWithWhiteSpaceAndSpecialCharacters_ReturnsExpectedResult() {
        String result = caverphone.encode("e$xampl12e");
        assertEquals("AMPL111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_SameStringInput_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStringInput_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("example1", "example2");
        assertFalse(result);
    }

}