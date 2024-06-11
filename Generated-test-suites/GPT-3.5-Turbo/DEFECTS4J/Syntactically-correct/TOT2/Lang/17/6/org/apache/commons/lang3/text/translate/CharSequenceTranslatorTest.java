import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CharSequenceTranslatorTest {

    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };

    @Test
    public void testTranslate() throws IOException {
        String input = "test input";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(input, writer.toString());
    }

    @Test
    public void testTranslateNullInput() {
        String output = translator.translate(null);
        assertEquals(null, output);
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
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65; // 'A'
        String hexString = CharSequenceTranslator.hex(codepoint);
        assertEquals("41", hexString);
    }
}