import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "abc";
        String expectedOutput = "abc";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertEquals(null, translator.translate(null));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "abc";
        String expectedOutput = "abc";
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter() {
            @Override
            public void write(String str) throws IOException {
                throw new IOException();
            }
        };
        String input = "abc";
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            // Exception is expected
        }
    }

    @Test
    public void testTranslateWithIOExceptionThrown() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        String input = "abc";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            // Exception is expected
        }
    }

    @Test
    public void testWithTranslators() {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MockCharSequenceTranslator();
        CharSequenceTranslator result = translator1.with(translator2);
        assertEquals(result.getClass(), AggregateTranslator.class);
    }

    @Test
    public void testHex() {
        int codepoint = 65; // Represents 'A'
        assertEquals("41", CharSequenceTranslator.hex(codepoint));
    }

    private static class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

}