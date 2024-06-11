import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        CharSequence input = "test";
        translator.translate(input, null);
    }

    @Test
    public void testTranslate_NullInput() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        Writer writer = mock(Writer.class);
        translator.translate(null, writer);
        verify(writer).write(null);
    }

    @Test
    public void testTranslate_NoTranslation() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        CharSequence input = "test";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslate_WithTranslation() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        CharSequence input = "test";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("translated test", writer.toString());
    }

    @Test
    public void testTranslate_UnicodeInput() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        CharSequence input = "\u0061\u0062\u0063";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("\u0061\u0062\u0063", writer.toString());
    }

    @Test
    public void testTranslate_NullInput_ReturnsNull() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testWith_MergeTranslators() throws IOException {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MockCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        CharSequence input = "test";
        Writer writer = new StringWriter();
        mergedTranslator.translate(input, writer);
        assertEquals("translated twice test", writer.toString());
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("61", CharSequenceTranslator.hex(97));
        assertEquals("75", CharSequenceTranslator.hex(117));
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            if (input.charAt(index) == 't' && input.charAt(index + 1) == 'e'
                    && input.charAt(index + 2) == 's' && input.charAt(index + 3) == 't') {
                out.write("translated");
                return 4;
            }
            if (input.charAt(index) == 'a' && input.charAt(index + 1) == 'b'
                    && input.charAt(index + 2) == 'c') {
                out.write(input.charAt(index));
                return 3;
            }
            return 0;
        }
    }
}