import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;



public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator translator = new CharSequenceTranslator() {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }

        @Test
        public void translateTest() throws IOException {
            CharSequence input = "test";
            Writer writer = new StringWriter();
            int consumed = translator.translate(input, 0, writer);
            Assert.assertEquals(0, consumed);
            Assert.assertEquals("test", writer.toString());
        }

        @Test
        public void translateNullTest() throws IOException {
            CharSequence input = null;
            Writer writer = new StringWriter();
            translator.translate(input, 0, writer);
            Assert.assertEquals("", writer.toString());
        }

        @Test
        public void translateStringTest() {
            CharSequence input = "test";
            String output = translator.translate(input);
            Assert.assertEquals("test", output);
        }

        @Test
        public void translateNullStringTest() {
            CharSequence input = null;
            String output = translator.translate(input);
            Assert.assertNull(output);
        }

        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }

        @Test
        public void hexTest() {
            int codepoint = 65;
            String hex = CharSequenceTranslator.hex(codepoint);
            Assert.assertEquals("41", hex);
        }

        @Test
        public void hexUpperCaseTest() {
            int codepoint = 97;
            String hex = CharSequenceTranslator.hex(codepoint);
            Assert.assertEquals("61", hex);
        }
    };

    @Test
    public void withTest() {
        CharSequenceTranslator[] translators = new CharSequenceTranslator[1];
        translators[0] = translator;
        CharSequenceTranslator result = translator.with(translators);
        Assert.assertNotNull(result);
    }
}

