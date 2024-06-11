import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        private int translateCalls = 0;

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null)
                throw new IllegalArgumentException("Null argument received");
            translateCalls++;
            return 0;
        }

        public int getTranslateCalls() {
            return translateCalls;
        }
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_NonNullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        String input = "testing translation";
        String expectedOutput = "testing translation";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslate_InvalidArguments() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        try {
            translator.translate("test", 0, null);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Exception expected
        }

        try {
            translator.translate(null, 0, new StringWriter());
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testTranslate_WriterNotNull() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        String input = "testing translation";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);

        String expectedOutput = input;
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_EmptyInput() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        String input = "";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);

        String expectedOutput = input;
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_TranslateCalls() throws IOException {
        MockCharSequenceTranslator translator = new MockCharSequenceTranslator();

        String input = "testing translation";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);

        int expectedTranslateCalls = 1;
        assertEquals(expectedTranslateCalls, translator.getTranslateCalls());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MockCharSequenceTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertNotNull(mergedTranslator);
        assertTrue((mergedTranslator instanceof CharSequenceTranslator));

        assertEquals(2, ((MockCharSequenceTranslator) mergedTranslator).getTranslateCalls());
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";

        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }

}