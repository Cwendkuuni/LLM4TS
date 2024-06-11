import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Hello World";
        String expectedOutput = "Hello World";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        String expectedOutput = null;
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expectedConsumed = 1;
        try {
            int actualConsumed = translator.translate(input, index, writer);
            assertEquals(expectedConsumed, actualConsumed);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTranslateMethodWithIOException() {
        CharSequenceTranslator translator = new TestTranslatorWithException();
        String input = "abc";
        StringWriter writer = new StringWriter();
        int index = 0;
        assertThrows(IOException.class, () -> translator.translate(input, index, writer));
    }

    @Test
    public void testHexConversion() {
        int codepoint = 65; // 'A' in Unicode
        String expectedHex = "41";
        String actualHex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, actualHex);
    }

    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequenceTranslator anotherTranslator = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator.with(anotherTranslator);
        assertNotNull(mergedTranslator);
    }

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

    private class TestTranslatorWithException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }
}