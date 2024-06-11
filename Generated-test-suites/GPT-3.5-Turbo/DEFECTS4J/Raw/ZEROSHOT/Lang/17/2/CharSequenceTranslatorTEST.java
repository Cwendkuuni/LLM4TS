import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String input = "test";
        String expected = "test";
        String result = translator.translate(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String input = null;
        String expected = null;
        String result = translator.translate(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String input = "test";
        String expected = "test";

        StringWriter writer = new StringWriter(input.length() * 2);
        translator.translate(input, writer);
        String result = writer.toString();
        Assert.assertEquals(expected, result);
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
        StringWriter writer = null;
        translator.translate(input, writer);
    }

    @Test
    public void testTranslateWithCodepoints() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String input = "test";
        String expected = "test";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        int len = Character.codePointCount(input, 0, input.length());
        while (pos < len) {
            int consumed = translator.translate(input, pos, stringBuilder);
            if (consumed == 0) {
                char[] c = Character.toChars(Character.codePointAt(input, pos));
                stringBuilder.append(c);
            } else {
                for (int pt = 0; pt < consumed; pt++) {
                    if (pos < len - 2) {
                        pos += Character.charCount(Character.codePointAt(input, pos));
                    } else {
                        pos++;
                    }
                }
                pos--;
            }
            pos++;
        }
        String result = stringBuilder.toString();
        Assert.assertEquals(expected, result);
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
        Assert.assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, result);
    }
}