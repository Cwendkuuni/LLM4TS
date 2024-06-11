import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = "Hello";
        String expected = "Hello";
        String actual = translator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = null;
        String expected = null;
        String actual = translator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = "Hello";
        String expected = "Hello";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = "Hello";
        StringWriter writer = null;
        translator.translate(input, writer);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("translator1: " + input.charAt(index));
                return 1;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("translator2: " + input.charAt(index));
                return 1;
            }
        };

        CharSequenceTranslator combinedTranslator = translator1.with(translator2);

        String input = "Hello";
        String expected = "translator1: translator2: Hello";
        String actual = combinedTranslator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String actual = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, actual);
    }

}