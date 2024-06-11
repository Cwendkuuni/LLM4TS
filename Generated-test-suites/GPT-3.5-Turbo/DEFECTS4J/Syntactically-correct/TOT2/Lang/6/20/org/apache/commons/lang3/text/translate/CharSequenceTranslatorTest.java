import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("test", 0, writer);
        assertEquals(4, result);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        String result = translator.translate("test");
        assertEquals("test", result);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        try {
            translator.translate("test", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithTranslators() {
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        CharSequenceTranslator newTranslator = translator.with(new SampleCharSequenceTranslator());
        assertNotNull(newTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("1F4A9", CharSequenceTranslator.hex(128169));
    }

    private class SampleCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }

    }
}