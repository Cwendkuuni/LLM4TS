import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expectedOutput1 = "h699119999";
        assertEquals(expectedOutput1, caverphone.caverphone(input1));

        // Edge case: empty string
        String input2 = "";
        String expectedOutput2 = "1111111111";
        assertEquals(expectedOutput2, caverphone.caverphone(input2));

        // Edge case: null input
        String input3 = null;
        String expectedOutput3 = "1111111111";
        assertEquals(expectedOutput3, caverphone.caverphone(input3));

        // Error scenario: special characters in input
        String input4 = "He#llo";
        String expectedOutput4 = "h699119999";
        assertEquals(expectedOutput4, caverphone.caverphone(input4));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expectedOutput1 = "h699119999";
        assertEquals(expectedOutput1, caverphone.encode(input1));

        // Edge case: empty string
        String input2 = "";
        String expectedOutput2 = "1111111111";
        assertEquals(expectedOutput2, caverphone.encode(input2));

        // Edge case: null input
        try {
            caverphone.encode(null);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception expected
        }

        // Error scenario: non-string input
        try {
            caverphone.encode(1234);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use case: same caverphone codes
        String input1 = "hello";
        String input2 = "Hello";
        assertTrue(caverphone.isCaverphoneEqual(input1, input2));

        // Typical use case: different caverphone codes
        String input3 = "code";
        String input4 = "coder";
        assertFalse(caverphone.isCaverphoneEqual(input3, input4));

        // Edge case: empty strings
        String input5 = "";
        String input6 = "";
        assertTrue(caverphone.isCaverphoneEqual(input5, input6));

        // Edge case: null input
        String input7 = null;
        String input8 = null;
        assertTrue(caverphone.isCaverphoneEqual(input7, input8));
    }
}