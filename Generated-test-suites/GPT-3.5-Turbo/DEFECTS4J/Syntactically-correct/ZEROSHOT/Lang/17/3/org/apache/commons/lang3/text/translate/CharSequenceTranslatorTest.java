import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();

        // Test null input
        Assert.assertNull(translator.translate(null));

        // Test empty input
        Assert.assertEquals("", translator.translate(""));

        // Test translation with Writer
        StringWriter writer = new StringWriter();
        translator.translate("Hello", writer);
        Assert.assertEquals("Translated: Hello", writer.toString());

        // Test translation without Writer
        Assert.assertEquals("Translated: Hello", translator.translate("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        translator.translate("Hello", null);
    }

    @Test
    public void testTranslateWithIndex() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();

        // Test translation at index 0
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(", World!", 0, writer);
        Assert.assertEquals(2, consumed);
        Assert.assertEquals("Translated: , ", writer.toString());

        // Test translation at index 5
        writer = new StringWriter();
        consumed = translator.translate(", World!", 5, writer);
        Assert.assertEquals(0, consumed);
        Assert.assertEquals(", World!", writer.toString());
    }

    @Test
    public void testTranslateWithIndexAndLargeInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();

        // Test translation at index 0 with large input
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 10_000; i++) {
            input.append("Hello, ");
        }
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input.toString(), 0, writer);
        Assert.assertEquals(28, consumed);
        Assert.assertEquals("Translated: Hello, ", writer.toString());

        // Test translation at index 5 with large input
        writer = new StringWriter();
        consumed = translator.translate(input.toString(), 5, writer);
        Assert.assertEquals(0, consumed);
        Assert.assertEquals(input.toString(), writer.toString());
    }

    @Test
    public void testWith() throws IOException {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        // Test merged translation
        StringWriter writer = new StringWriter();
        mergedTranslator.translate("Hello", writer);
        Assert.assertEquals("Translated: Translated: Hello", writer.toString());

        // Test individual translators
        writer = new StringWriter();
        translator1.translate("Hello", writer);
        Assert.assertEquals("Translated: Hello", writer.toString());

        writer = new StringWriter();
        translator2.translate("Hello", writer);
        Assert.assertEquals("Translated: Hello", writer.toString());
    }

    @Test
    public void testHex() {
        // Test hexadecimal conversion
        Assert.assertEquals("41", CharSequenceTranslator.hex(65)); // 'A'
        Assert.assertEquals("7A", CharSequenceTranslator.hex(122)); // 'z'
        Assert.assertEquals("D83D", CharSequenceTranslator.hex(55357));
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index >= input.length()) {
                return 0;
            }

            out.write("Translated: ");
            out.write(input.charAt(index));
            out.write(" ");
            return 1;
        }
    }
}