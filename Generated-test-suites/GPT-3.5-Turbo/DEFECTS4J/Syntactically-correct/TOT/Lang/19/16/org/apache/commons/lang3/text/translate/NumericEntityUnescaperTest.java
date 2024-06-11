import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedCodepoint() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65;"); // ASCII code for 'A'
        MockWriter writer = new MockWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(4, result);
        assertEquals("A", writer.getOutput());
    }

    @Test
    public void testTranslate_withValidHexInput_shouldReturnTranslatedCodepoint() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#x1F609;"); // Unicode codepoint for '😉'
        MockWriter writer = new MockWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(10, result);
        assertEquals("😉", writer.getOutput());
    }

    @Test
    public void testTranslate_withMissingClosingSemicolon_shouldReturnZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65"); // Missing closing semicolon for 'A'
        MockWriter writer = new MockWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.getOutput());
    }

    @Test
    public void testTranslate_withInvalidNumericCode_shouldReturnZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#ABC;"); // Invalid numeric code
        MockWriter writer = new MockWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.getOutput());
    }

    @Test
    public void testTranslate_withInvalidHexCode_shouldReturnZero() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#xG;"); // Invalid hexadecimal code
        MockWriter writer = new MockWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.getOutput());
    }

    private class MockWriter extends Writer {

        private StringBuilder output;

        public MockWriter() {
            output = new StringBuilder();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            output.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            // Not needed for testing
        }

        @Override
        public void close() throws IOException {
            // Not needed for testing
        }

        public String getOutput() {
            return output.toString();
        }
    }
}