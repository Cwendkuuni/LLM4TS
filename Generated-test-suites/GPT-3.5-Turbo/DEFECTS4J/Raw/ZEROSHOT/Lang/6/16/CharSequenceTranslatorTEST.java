import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate1() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequence input = "test";
        Writer out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate2() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequence input = "test";
        String result = translator.translate(input);
        assertEquals("test", result);
    }

    @Test
    public void testTranslate3() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('a');
                return 1;
            }
        };

        CharSequence input = "test";
        Writer out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(1, result);
        assertEquals("a", out.toString());
    }

    @Test
    public void testTranslate4() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 2;
            }
        };

        CharSequence input = "test";
        Writer out = new StringWriter();
        int result = translator.translate(input, 0, out);
        assertEquals(2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate5() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequence input = "test";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator[] translators = { translator };
        CharSequenceTranslator result = translator.with(translators);

        assertNotNull(result);
    }

    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(65);
        assertEquals("41", result);
    }
}