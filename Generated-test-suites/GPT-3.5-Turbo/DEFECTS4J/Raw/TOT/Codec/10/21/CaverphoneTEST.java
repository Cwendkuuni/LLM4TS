import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expected1 = "HLL1111111";
        assertEquals(expected1, caverphone.caverphone(input1));

        // Edge case: empty string
        String input2 = "";
        String expected2 = "1111111111";
        assertEquals(expected2, caverphone.caverphone(input2));

        // Error scenario: null input
        String input3 = null;
        String expected3 = "1111111111";
        assertEquals(expected3, caverphone.caverphone(input3));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String expected1 = "HLL1111111";
        assertEquals(expected1, caverphone.encode(input1));

        // Error scenario: non-string input
        try {
            caverphone.encode(new Object());
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception is expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        String input1 = "hello";
        String input2 = "Hello";
        assertTrue(caverphone.isCaverphoneEqual(input1, input2));

        // Edge case: empty strings
        String input3 = "";
        String input4 = "";
        assertTrue(caverphone.isCaverphoneEqual(input3, input4));

        // Typical use case: different caverphones
        String input5 = "world";
        String input6 = "World";
        assertFalse(caverphone.isCaverphoneEqual(input5, input6));
    }
}