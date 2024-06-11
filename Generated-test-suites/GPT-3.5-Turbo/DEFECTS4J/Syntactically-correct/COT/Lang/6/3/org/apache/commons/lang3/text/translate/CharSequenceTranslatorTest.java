import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Test null input
        assertNull(translator.translate(null));

        // Test empty input
        assertEquals("", translator.translate(""));

        // Test basic translation
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslate_withWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Test null input
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());

        // Test empty input
        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());

        // Test basic translation
        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTranslate_withNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        translator.translate("abc", null);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator anotherTranslator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator merged = translator.with(anotherTranslator);
        assertNotNull(merged);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
        assertEquals("100", CharSequenceTranslator.hex(256));
    }
}