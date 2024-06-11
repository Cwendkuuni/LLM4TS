import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_basicFunctionality() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("hello", 0, writer);
        assertEquals(5, result);
        assertEquals("hello", writer.toString());
    }

    @Test
    public void testTranslate_inputNull() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate(null, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_allCodepointsTranslated() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("\uD835\uDC3A\uD835\uDC3B\uD835\uDC3C", 0, writer);
        assertEquals(6, result);
        assertEquals("XYZ", writer.toString());
    }

    @Test
    public void testTranslate_withException() {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate("test", 0, null);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (IOException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testTranslate_negativeIndex() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("hello", -1, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_emptyInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }
            if (index >= 0 && index < input.length()) {
                out.write(input.charAt(index));
                return 1;
            }
            return 0;
        }
    }
}