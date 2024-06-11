import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        // Typical use case
        CharSequenceTranslator translator = new MockTranslator();
        String input = "This is a test.";
        String expectedOutput = "Th%%is is a test.";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Edge case
        input = "";
        expectedOutput = "";
        actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Error scenario
        input = null;
        expectedOutput = null;
        actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        // Typical use case
        CharSequenceTranslator translator = new MockTranslator();
        String input = "This is a test.";
        String expectedOutput = "Th%%is is a test.";
        Writer writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        writer.flush();
        String actualOutput = writer.toString();
        Assert.assertEquals(input.length(), consumed);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Edge case
        input = "";
        expectedOutput = "";
        writer = new StringWriter();
        consumed = translator.translate(input, 0, writer);
        writer.flush();
        actualOutput = writer.toString();
        Assert.assertEquals(input.length(), consumed);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Error scenario
        input = null;
        expectedOutput = null;
        writer = new StringWriter();
        consumed = translator.translate(input, 0, writer);
        writer.flush();
        actualOutput = writer.toString();
        Assert.assertEquals(0, consumed);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHex() {
        // Typical use case
        int codepoint = 268;
        String expectedOutput = "10C";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Edge case
        codepoint = 0;
        expectedOutput = "0";
        actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Error scenario
        codepoint = -1;
        expectedOutput = "FFFFFFFF";
        actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    private class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'T' && input.charAt(index + 1) == 'h') {
                out.write("Th%%");
                return 4;
            } else {
                out.write(input.charAt(index));
                return 1;
            }
        }
    }

}