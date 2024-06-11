import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < 0 || index >= input.length()) {
                throw new IndexOutOfBoundsException();
            }
            out.write(input.charAt(index));
            return 1;
        }
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_withValidInput_shouldTranslateCorrectly() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslate_withMultipleTranslators_shouldTranslateCorrectly() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();
        CharSequenceTranslator translator3 = new DummyTranslator();
        CharSequenceTranslator translator = translator1.with(translator2, translator3);
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslate_withInvalidTranslator_shouldThrowException() {
        CharSequenceTranslator translator = new DummyTranslator();
        try {
            translator.with(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testTranslate_withIndexOutOfBounds_shouldThrowException() {
        CharSequenceTranslator translator = new DummyTranslator();
        try {
            translator.translate("abc", 3, new StringWriter());
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}