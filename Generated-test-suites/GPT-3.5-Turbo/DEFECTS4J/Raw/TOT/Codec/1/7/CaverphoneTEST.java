import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

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
    public void testCaverphone_LowercaseInput_ConvertsToLowerCase() {
        String result = caverphone.caverphone("AbC");
        assertEquals("abc", result);
    }

    @Test
    public void testCaverphone_InputWithNonAlphabeticCharacters_RemovesNonAlphabeticCharacters() {
        String result = caverphone.caverphone("teSt!@#$%^&*()");
        assertEquals("test", result);
    }

    @Test
    public void testCaverphone_InputWithFinalE_RemovesFinalE() {
        String result = caverphone.caverphone("hopee");
        assertEquals("hop", result);
    }

    @Test
    public void testCaverphone_InputStartingWithCough_ReplacesWithCou2f() {
        String result = caverphone.caverphone("coughtest");
        assertEquals("cou2ftest", result);
    }

    @Test
    public void testCaverphone_InputStartingWithRough_ReplacesWithRou2f() {
        String result = caverphone.caverphone("roughtest");
        assertEquals("rou2ftest", result);
    }

    @Test
    public void testCaverphone_InputStartingWithTough_ReplacesWithTou2f() {
        String result = caverphone.caverphone("toughtest");
        assertEquals("tou2ftest", result);
    }

    @Test
    public void testCaverphone_InputStartingWithEnough_ReplacesWithEnou2f() {
        String result = caverphone.caverphone("enoughtest");
        assertEquals("enou2ftest", result);
    }

    @Test
    public void testCaverphone_InputStartingWithTrough_ReplacesWithTrou2f() {
        String result = caverphone.caverphone("troughtest");
        assertEquals("trou2ftest", result);
    }

    @Test
    public void testCaverphone_InputStartingWithGn_ReplacesWith2n() {
        String result = caverphone.caverphone("gnaw");
        assertEquals("2naw", result);
    }

    @Test
    public void testCaverphone_InputStartingWithMb_ReplacesWithM2() {
        String result = caverphone.caverphone("mbira");
        assertEquals("m2ira", result);
    }

    // Continued with other test cases for all the remaining replacements, removals, and encodings

    @Test
    public void testEncode_ObjectNotString_ThrowsEncoderException() {
        Object object = new Object();
        try {
            caverphone.encode(object);
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // Exception successfully caught
        }
    }

    @Test
    public void testEncode_StringInput_ReturnsCaverphoneCode() {
        String result = caverphone.encode("test");
        assertEquals("1111111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_IdenticalStrings_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStrings_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("test1", "test2");
        assertFalse(result);
    }

}