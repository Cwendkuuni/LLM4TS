import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("Hello", 0, writer);
        assertEquals(5, result);
        assertEquals("Test: Hello", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate(null, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        translator.translate("Hello", 0, null);
    }

    @Test
    public void testTranslateStringInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("Hello");
        assertEquals("Test: Hello", result);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Test: ");
            out.write(input.subSequence(index, input.length()).toString());
            return input.length() - index;
        }
    }
}