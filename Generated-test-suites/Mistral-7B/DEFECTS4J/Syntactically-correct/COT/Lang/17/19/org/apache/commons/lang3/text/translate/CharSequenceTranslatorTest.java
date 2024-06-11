import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Test for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World";
        String expectedOutput = "48656c6c6f20576f726c64";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String actualOutput = out.toString();
        String expectedOutput = "48656c6c6f20576f726c64";
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "";
        String actualOutput = translator.translate(input);
        String expectedOutput = "";
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new AnotherCharSequenceTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        CharSequence input = "Hello World";
        String expectedOutput = "48656c6c6f20576f726c64XX"; // XX represents the output of AnotherCharSequenceTranslator
        String actualOutput = aggregateTranslator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    private static class MyCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(hex(Character.codePointAt(input, index)).toCharArray());
            int consumed = Character.charCount(Character.codePointAt(input, index));
            return consumed;
        }
    }

    private static class AnotherCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("XX".toCharArray());
            return 2;
        }
    }
}