import org.junit.Assert;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    // Test translate method

    // Typical use case: Translate a string with a translator that replaces certain characters
    @Test
    public void testTranslate_typical() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "Hello, world!";
        String expectedOutput = "H+++";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // Edge case: Translate an empty string
    @Test
    public void testTranslate_emptyString() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // Error scenario: Translate a null string
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_nullString() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = null;
        translator.translate(input);
    }

    // Test translate method with index and out parameters

    // Typical use case: Translate a string with a translator that replaces certain characters
    @Test
    public void testTranslateWithIndexAndOut_typical() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "Hello, world!";
        String expectedOutput = "H+++";
        StringBuilder actualOutputBuilder = new StringBuilder();
        int consumed = translator.translate(input, 0, actualOutputBuilder);
        Assert.assertEquals(expectedOutput.length(), consumed);
        Assert.assertEquals(expectedOutput, actualOutputBuilder.toString());
    }

    // Edge case: Translate an empty string
    @Test
    public void testTranslateWithIndexAndOut_emptyString() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "";
        String expectedOutput = "";
        StringBuilder actualOutputBuilder = new StringBuilder();
        int consumed = translator.translate(input, 0, actualOutputBuilder);
        Assert.assertEquals(expectedOutput.length(), consumed);
        Assert.assertEquals(expectedOutput, actualOutputBuilder.toString());
    }

    // Error scenario: Translate a null string
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithIndexAndOut_nullString() {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = null;
        StringBuilder actualOutputBuilder = new StringBuilder();
        translator.translate(input, 0, actualOutputBuilder);
    }

    // Helper method tests

    // Typical use case: Hexadecimal conversion for a codepoint
    @Test
    public void testHex_typical() {
        int codepoint = 65;
        String expectedOutput = "41";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // Edge case: Hexadecimal conversion for the lowest possible codepoint (0)
    @Test
    public void testHex_lowestCodePoint() {
        int codepoint = 0;
        String expectedOutput = "0";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // Edge case: Hexadecimal conversion for the highest possible codepoint (1114111)
    @Test
    public void testHex_highestCodePoint() {
        int codepoint = 1114111;
        String expectedOutput = "10FFFF";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    // End of test cases

    // Custom Translator for testing
    private static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            for (int i = index; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == 'e') {
                    out.write('+');
                } else if (c == 'o') {
                    out.write('3');
                }
            }
            return input.length() - index;
        }
    }

}