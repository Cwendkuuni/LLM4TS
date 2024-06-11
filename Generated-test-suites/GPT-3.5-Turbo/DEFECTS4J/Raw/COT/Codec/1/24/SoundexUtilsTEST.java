
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Basic test case
        String input = "abc123";
        String expectedOutput = "ABC";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        // Test with empty string
        input = "";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        // Test with null input
        input = null;
        expectedOutput = null;
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        // Test with input containing special characters
        input = "#$%^&*";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference() throws EncoderException {
        // Basic test case
        StringEncoder encoder = new Soundex();
        String input1 = "hello";
        String input2 = "jello";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoder, input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with null inputs
        input1 = null;
        input2 = null;
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with empty string inputs
        input1 = "";
        input2 = "";
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with inputs of different lengths
        input1 = "abc";
        input2 = "abcd";
        expectedOutput = 3;
        actualOutput = SoundexUtils.difference(encoder, input1, input2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded() {
        // Basic test case
        String input1 = "0123";
        String input2 = "0124";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.differenceEncoded(input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with null inputs
        input1 = null;
        input2 = null;
        expectedOutput = 0;
        actualOutput = SoundexUtils.differenceEncoded(input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with empty string inputs
        input1 = "";
        input2 = "";
        expectedOutput = 0;
        actualOutput = SoundexUtils.differenceEncoded(input1, input2);
        assertEquals(expectedOutput, actualOutput);

        // Test with inputs of different lengths
        input1 = "abc";
        input2 = "abcd";
        expectedOutput = 3;
        actualOutput = SoundexUtils.differenceEncoded(input1, input2);
        assertEquals(expectedOutput, actualOutput);
    }
}
