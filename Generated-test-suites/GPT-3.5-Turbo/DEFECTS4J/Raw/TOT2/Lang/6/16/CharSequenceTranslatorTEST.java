import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "abc";
        String expected = "abc";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new DummyTranslatorWithException();
        String input = "abc";
        assertThrows(RuntimeException.class, () -> translator.translate(input));
    }

    @Test
    public void testTranslateWithWriterNull() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "abc";
        assertThrows(IllegalArgumentException.class, () -> translator.translate(input, null));
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

    private static class DummyTranslatorWithException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }
}