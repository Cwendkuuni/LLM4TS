package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        String input = "test";
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        assert(writer.toString().equals("t"));
    }

    @Test
    public void testTranslateWithInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        assert(translator.translate(input) == null);
    }

    @Test
    public void testTranslateWithInputAndWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        String input = "test";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assert(writer.toString().equals("t"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithNullInputAndOutput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = null;
        assert(translator.translate(input) == null);
    }

    @Test
    public void testHex() {
        assert(CharSequenceTranslator.hex(10) == "A");
    }
}