import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        assertNull(translator.translate(null));

        String input = "Hello";
        assertEquals(input, translator.translate(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        translator.translate("Hello", null);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        StringWriter writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithNonEmptyInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        StringWriter writer = new StringWriter();
        translator.translate("Hello", writer);
        assertEquals("Hello", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertArrayEquals(new CharSequenceTranslator[]{translator1, translator2}, ((AggregateTranslator) mergedTranslator).getTranslators());
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("4D", CharSequenceTranslator.hex(77));
    }
}