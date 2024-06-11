package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String source = "Hello, 世界!";
        String expected = "Hello,\\uD840\\uDC00\\uD840\\uDC0D!";
        StringWriter target = new StringWriter();

        int consumed = translator.translate(source, 0, target);
        String result = target.toString();

        assertEquals(expected, result);
        assertEquals(source.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateWithStringWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String source = "Hello, 世界!";

        String result = translator.translate(source);

        String expected = "Hello,\\uD840\\uDC00\\uD840\\uDC0D!";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null CharSequence
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullCharSequence() {
        CharSequenceTranslator translator = new DefaultTranslator();
        Writer writer = new StringWriter();

        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence charSequence = "Hello, 世界!";

        translator.translate(charSequence, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with IOException
     */
    @Test(expected = IOException.class)
    public void testTranslateWithIOException() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        String source = "Hello, 世界!";
        StringWriter target = new StringWriter();

        translator.translate(source, target);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty CharSequence
     */
    @Test
    public void testTranslateWithEmptyCharSequence() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter target = new StringWriter();

        int consumed = translator.translate("", 0, target);
        String result = target.toString();

        assertEquals("", result);
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with CharSequence containing only whitespace
     */
    @Test
    public void testTranslateWithCharSequenceContainingOnlyWhitespace() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String source = "   ";
        StringWriter target = new StringWriter();

        int consumed = translator.translate(source, 0, target);
        String result = target.toString();

        assertEquals("", result);
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with input longer than input length
     */
    @Test
    public void testTranslateWithInputLongerThanInputLength() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String source = "Hello, 世界!";
        StringWriter target = new StringWriter();

        int consumed = translator.translate(source, source.length(), target);
        String result = target.toString();

        assertEquals("", result);
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null CharSequence
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullCharSequenceInTranslateMethod() {
        CharSequenceTranslator translator = new DefaultTranslator();

        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty CharSequence
     */
    @Test
    public void testTranslateWithEmptyCharSequence() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String result = translator.translate("");

        assertEquals("", result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with CharSequence containing only whitespace
     */
    @Test
    public void testTranslateWithCharSequenceContainingOnlyWhitespaceInTranslateMethod() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String result = translator.translate("   ");

        assertEquals("", result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer in translate method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriterInTranslateMethod() {
        CharSequenceTranslator translator = new DefaultTranslator() {
            @Override
            public void translate(CharSequence input, Writer out) throws IOException {
                out = null;
            }
        };
        String source = "Hello, 世界!";

        translator.translate(source, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty StringWriter
     */
    @Test
    public void testTranslateWithEmptyStringWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter();

        String result = translator.translate("Hello, 世界!");
        String expected = "Hello,\\uD840\\uDC00\\uD840\\uDC0D!";

        translator.translate(result, writer);
        assertEquals(expected, writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with Writer with limited capacity
     */
    @Test
    public void testTranslateWithWriterWithLimitedCapacity() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter(3);

        String source = "Hello, 世界!";

        assertThrows(IllegalArgumentException.class, () -> {
            translator.translate(source, writer);
        });
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with Writer with null character encoding
     */
    @Test
    public void testTranslateWithWriterWithNullCharacterEncoding() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter();

        assertThrows(NullPointerException.class, () -> {
            translator.translate(null, writer);
        });
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with CharSequence equal to its translation
     */
    @Test
    public void testTranslateWithCharSequenceEqualToItsTranslation() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String source = "Hello\\uD840\\uDC00\\uD840\\uDC0D!";

        String result = translator.translate(source);

        assertEquals(source, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple translators
     */
    @Test
    public void testTranslateWithMultipleTranslators() throws IOException {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("2");
                return 1;
            }
        };
        CharSequenceTranslator translator3 = new DefaultTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);
        String source = "Hello, 世界!";
        StringWriter target = new StringWriter();

        translator1.translate(source, target);
        String result1 = target.toString();

        translator1.translate(source, target);
        String result2 = target.toString();

        assertEquals(result1, result2);
        assertEquals("Hello,\\uD840\\uDC00\\uD840\\uDC0D!2", result1);
    }
}