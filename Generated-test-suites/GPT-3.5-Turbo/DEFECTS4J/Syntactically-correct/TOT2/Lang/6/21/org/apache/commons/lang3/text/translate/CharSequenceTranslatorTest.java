import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithStringWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("test", 0, writer);
        assertEquals(4, consumed);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateToNullWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate("test", null));
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new TestTranslator();
        assertThrows(IOException.class, () -> translator.translate("test", 0, new TestWriter()));
    }

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals(1, translator.translate("abc", 0, new StringWriter()));
        assertEquals(0, translator.translate("def", 1, new StringWriter()));
    }

    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHexMethod() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F4", CharSequenceTranslator.hex(500));
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'x') {
                out.write("X");
                return 1;
            }
            return 0;
        }
    }

    private static class TestWriter extends StringWriter {
        @Override
        public void write(String str) throws IOException {
            throw new IOException("Test IOException");
        }
    }

}