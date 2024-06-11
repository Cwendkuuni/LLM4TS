import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
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
    public void testCaverphone_LowercaseLettersOnly_ReturnsExpectedResult() {
        String result = caverphone.caverphone("abcdefghijklmnopqrstuvwxyz");
        assertEquals("31211111111111131", result);
    }

    @Test
    public void testCaverphone_UppercaseLettersOnly_ReturnsExpectedResult() {
        String result = caverphone.caverphone("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("31211111111111131", result);
    }

    @Test
    public void testCaverphone_MixedCaseLetters_ReturnsExpectedResult() {
        String result = caverphone.caverphone("AbCdEfGhIjKlMnOpQrStUvWxYz");
        assertEquals("31211111111111131", result);
    }

    @Test
    public void testCaverphone_PunctuationAndSpecialCharacters_ReturnsExpectedResult() {
        String result = caverphone.caverphone("!@#$%^&*()_+-=[]{}|;:',.<>/?");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncode_ObjectOfDifferentType_ExceptionThrown() {
        try {
            Object result = caverphone.encode(new Integer(123));
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException ex) {
            // Pass
        }
    }

    @Test
    public void testEncode_InputString_ReturnsExpectedResult() {
        String result = caverphone.encode("Testing");
        assertEquals("3121111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_IdenticalStrings_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("Test", "Test");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStrings_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("Test", "Test2");
        assertFalse(result);
    }

}