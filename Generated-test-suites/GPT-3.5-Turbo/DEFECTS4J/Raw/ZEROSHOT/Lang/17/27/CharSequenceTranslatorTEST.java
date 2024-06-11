import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new TestTranslator();
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    @Test
    public void testTranslate_NullInput() {
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_EmptyInput() {
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_WithInput() {
        assertEquals("Test", translator.translate("Test"));
    }

    @Test
    public void testTranslate_WithIOException() {
        CharSequenceTranslator translator = new ThrowableTranslator();

        try {
            translator.translate("Test");
            fail("IOException should have been thrown");
        } catch (IOException e) {
            // Success
        }
    }

    private static class ThrowableTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }

    @Test
    public void testWith_OneTranslator() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator result = translator.with(translator1);

        assertEquals(translator, result);
    }

    @Test
    public void testWith_MultipleTranslators() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator translator3 = new TestTranslator();

        CharSequenceTranslator result = translator.with(translator1, translator2, translator3);

        assertNotEquals(translator, result); // Verify new instance is created
        assertTrue(result instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

}