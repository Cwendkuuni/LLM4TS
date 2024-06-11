
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("[TRANSLATED]");
            return 1;
        }
    }

    @Test
    public void translationTest() throws IOException {
        TestTranslator translator = new TestTranslator();
        String input = "test";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        assertEquals(1, consumed);
        assertEquals("[TRANSLATED]", writer.toString());
    }

    @Test
    public void translateStringTest() {
        TestTranslator translator = new TestTranslator();
        String input = "test";
        String translation = translator.translate(input);
        assertEquals("[TRANSLATED]", translation);
    }

    @Test
    public void translateNullStringTest() {
        TestTranslator translator = new TestTranslator();
        String input = null;
        String translation = translator.translate(input);
        assertEquals(null, translation);
    }

    @Test
    public void translateWithNullWriterTest() {
        TestTranslator translator = new TestTranslator();
        String input = "test";
        boolean exceptionCaught = false;
        try {
            translator.translate(input, null);
        } catch (IllegalArgumentException e) {
            exceptionCaught = true;
            assertEquals("The Writer must not be null", e.getMessage());
        }
        assertEquals(true, exceptionCaught);
    }

    @Test
    public void translateWithEmptyInputTest() throws IOException {
        TestTranslator translator = new TestTranslator();
        String input = "";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }

    @Test
    public void hexConversionTest() {
        String hex = CharSequenceTranslator.hex(10);
        assertEquals("A", hex);
    }
}

