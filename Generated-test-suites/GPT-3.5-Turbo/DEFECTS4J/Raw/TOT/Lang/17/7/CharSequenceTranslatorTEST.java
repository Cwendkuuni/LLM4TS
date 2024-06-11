import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical Use Case
        CharSequenceTranslator translator = new MockTranslator();
        String translated = translator.translate("Hello World");
        assertEquals("Hello World", translated);

        // Edge Case
        translated = translator.translate("");
        assertEquals("", translated);

        // Error Scenario
        translated = translator.translate(null);
        assertNull(translated);
    }

    @Test
    public void testTranslateWithIndexAndWriter() {
        // Typical Use Case
        CharSequenceTranslator translator = new MockTranslator();
        MockWriter writer = new MockWriter();
        int count = translator.translate("Hello", 0, writer);
        assertEquals(5, count);

        // Edge Case
        writer.reset();
        count = translator.translate("", 0, writer);
        assertEquals(0, count);

        // Error Scenario
        writer.reset();
        try {
            count = translator.translate(null, 0, writer);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testTranslateWithWriter() {
        // Typical Use Case
        CharSequenceTranslator translator = new MockTranslator();
        MockWriter writer = new MockWriter();
        try {
            translator.translate("Hello", writer);
            assertEquals("Hello", writer.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Edge Case
        writer.reset();
        try {
            translator.translate("", writer);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Error Scenario
        writer.reset();
        try {
            translator.translate(null, writer);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWith() {
        // Typical Use Case
        CharSequenceTranslator translator1 = new MockTranslator();
        CharSequenceTranslator translator2 = new MockTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHex() {
        // Typical Use Case
        String hex = CharSequenceTranslator.hex(65);
        assertEquals("41", hex);
    }

    private static class MockTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length() || out == null) {
                throw new IllegalArgumentException("Invalid arguments");
            }

            if (index == 0) {
                out.write(input.toString());
                return input.length();
            } else {
                return 0;
            }
        }
    }

    private static class MockWriter extends Writer {
        private StringBuilder builder = new StringBuilder();

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            for (int i = off; i < off + len; i++) {
                builder.append(cbuf[i]);
            }
        }

        @Override
        public void flush() throws IOException {}

        @Override
        public void close() throws IOException {}

        public void reset() {
            builder = new StringBuilder();
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}