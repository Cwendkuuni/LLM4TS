import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SoundexUtilsTest {

    @Before
    public void setUp() {
        // Set up any necessary objects before running the test methods
    }

    // Test case for the clean() method
    @Test
    public void testClean() {
        // Typical use case - input string with only upper case letters
        String input1 = "HELLO";
        String expectedOutput1 = "HELLO";
        assertEquals(expectedOutput1, SoundexUtils.clean(input1));

        // Typical use case - input string with upper case and lower case letters
        String input2 = "Hello";
        String expectedOutput2 = "HELLO";
        assertEquals(expectedOutput2, SoundexUtils.clean(input2));

        // Edge case - input string is empty
        String input3 = "";
        String expectedOutput3 = "";
        assertEquals(expectedOutput3, SoundexUtils.clean(input3));

        // Edge case - input string is null
        String input4 = null;
        String expectedOutput4 = null;
        assertEquals(expectedOutput4, SoundexUtils.clean(input4));
    }

    // Test case for the difference() method
    @Test
    public void testDifference() throws EncoderException {
        // Typical use case - input strings have some characters that are the same
        StringEncoder encoder = new Soundex();
        String input1 = "APPLE";
        String input2 = "AMPLT";
        int expectedOutput1 = 3;
        assertEquals(expectedOutput1, SoundexUtils.difference(encoder, input1, input2));

        // Typical use case - input strings have no characters that are the same
        String input3 = "CAT";
        String input4 = "DOG";
        int expectedOutput2 = 0;
        assertEquals(expectedOutput2, SoundexUtils.difference(encoder, input3, input4));

        // Edge case - input strings are null
        String input5 = null;
        String input6 = null;
        int expectedOutput3 = 0;
        assertEquals(expectedOutput3, SoundexUtils.difference(encoder, input5, input6));

        // Error scenario - EncoderException when encoding one of the strings
        String input7 = "APPLE";
        String input8 = "AMPLT";
        StringEncoder encoderMock = new StringEncoderMock(); // Mocking the encoder
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoderMock, input7, input8));
    }

    // Test case for the differenceEncoded() method
    @Test
    public void testDifferenceEncoded() {
        // Typical use case - encoded strings have some characters that are the same
        String input1 = "A123";
        String input2 = "A124";
        int expectedOutput1 = 3;
        assertEquals(expectedOutput1, SoundexUtils.differenceEncoded(input1, input2));

        // Typical use case - encoded strings have no characters that are the same
        String input3 = "B123";
        String input4 = "C456";
        int expectedOutput2 = 0;
        assertEquals(expectedOutput2, SoundexUtils.differenceEncoded(input3, input4));

        // Edge case - encoded strings are null
        String input5 = null;
        String input6 = null;
        int expectedOutput3 = 0;
        assertEquals(expectedOutput3, SoundexUtils.differenceEncoded(input5, input6));
    }

    // Mock class to simulate a StringEncoder with EncoderException
    private class StringEncoderMock implements StringEncoder {
        @Override
        public String encode(String source) throws EncoderException {
            throw new EncoderException("Mocked EncoderException");
        }
    }

}