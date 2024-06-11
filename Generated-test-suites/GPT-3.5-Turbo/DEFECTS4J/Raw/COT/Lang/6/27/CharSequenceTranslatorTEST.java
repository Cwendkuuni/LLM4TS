
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        String result = new TestTranslator().translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        String result = new TestTranslator().translate("");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedString() {
        String result = new TestTranslator().translate("Hello, world!");
        assertNotNull(result);
        assertEquals("Translated: Hello, world!", result);
    }

    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        TestTranslator translator = new TestTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Custom IOException");
            }
        };
        try {
            translator.translate("Hello, world!");
        } catch (RuntimeException e) {
            assertEquals("Custom IOException", e.getCause().getMessage());
        }
    }

    private static class TestTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Translated: " + input.subSequence(index, input.length()));
            return input.length() - index;
        }
    }

}
