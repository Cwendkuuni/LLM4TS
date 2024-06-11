package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.TranslateException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test instance of CharSequenceTranslator.
     */
    private CharSequenceTranslator translator = new MyCharSequenceTranslator();

    /**
     * Test method for translate(CharSequence, Writer).
     *
     * @throws IOException whenWriter produces an IOException
     */
    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        String input = "Hello 世界!";
        CharSequence cs = CharSequence.of(input);
        StringWriter writer = new StringWriter();
        int actualConsumed = translator.translate(cs, writer);
        String expected = "Hello %5B%68%65%6C%6C%6F%20%E4%B8%96%E7%90%83%21";
        String result = writer.toString();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(input.length(), actualConsumed);
    }

    /**
     * Test method for translate(CharSequence).
     */
    @Test
    public void testTranslateCharSequence() {
        String input = "Hello 世界!";
        String expected = "Hello %5B%68%65%6C%6C%6F%20%E4%B8%96%E7%90%83%21";
        String result = translator.translate(CharSequence.of(input));
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for translate(CharSequence, Writer) with null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateCharSequenceWriterNull() {
        String input = "Hello 世界!";
        CharSequence cs = CharSequence.of(input);
        Writer out = null;
        translator.translate(cs, out);
    }

    /**
     * Test method for translate(CharSequence, Writer) with IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateCharSequenceWriterIOException() throws IOException {
        String input = "Hello 世界!";
        CharSequence cs = CharSequence.of(input);
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Writer simulation error.");
            }
        };
        translator.translate(cs, writer);
    }

    /**
     * Test method for translate(CharSequence) with null CharSequence.
     */
    @Test
    public void testTranslateCharSequenceNull() {
        CharSequence cs = null;
        String result = translator.translate(cs);
        Assert.assertNull(result);
    }

    /**
     * Test method for translate(CharSequence, Writer) with empty CharSequence.
     */
    @Test
    public void testTranslateCharSequenceEmpty() {
        String result = translator.translate(CharSequence.empty());
        Assert.assertEquals("", result);
    }

    /**
     * Test method for with(CharSequenceTranslator...).
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator1();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator2();
        CharSequenceTranslator mergedTranslator = new MyCharSequenceTranslator().with(translator1, translator2);
        String input = "Hello 世界!";
        String expected = "Hello %5B%68%65%6C%6C%6F%20%E4%B8%96%E7%90%83%21";
        String result = mergedTranslator.translate(CharSequence.of(input));
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for hex(int).
     */
    @Test
    public void testHex() {
        int codepoint = 0x1F00;
        String expected = "%1F00";
        String result = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expected, result);
    }
}

class MyCharSequenceTranslator extends CharSequenceTranslator {
    // Empty implementation.
}

class MyCharSequenceTranslator1 extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write(c);
        return Character.charCount(c);
    }
}

class MyCharSequenceTranslator2 extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new TranslateException("Simulated error.");
    }
}