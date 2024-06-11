package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.StandardTranslator;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * @throws IOException
     */
    @Test
    public void testTranslateMethod() throws IOException {
        CharSequenceTranslator translator = new StandardTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        String input = "Test String with special chars: ☠👻👽🌌";
        int index = 0;
        int codePointsConsumed = translator.translate(input, index, out);
        index += codePointsConsumed;

        assertEquals("Test String with special chars: \\u2620\\u2640\\u2640\\u2600", out.getBuffer().toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateMethodWithoutWriter() {
        CharSequenceTranslator translator = new StandardTranslator();
        String result = translator.translate("Test String with special chars: ☠👻👽🌌");

        assertEquals("Test String with special chars: \\u2620\\u2640\\u2640\\u2600", result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
     */
    @Test(expected=IllegalArgumentException.class)
    public void testTranslateMethodWithNullWriter() {
        CharSequenceTranslator translator = new StandardTranslator();
        translator.translate(null, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with negative index
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void testTranslateMethodNegativeIndex() throws IOException {
        CharSequenceTranslator translator = new StandardTranslator();
        CharSequence input = "Test String with special chars: ☠👻👽🌌";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int index = -1;
        translator.translate(input, index, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with null input
     */
    @Test(expected=NullPointerException.class)
    public void testTranslateMethodWithNullInput() throws IOException {
        CharSequenceTranslator translator = new StandardTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        translator.translate(null, 0, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input
     */
    @Test(expected=NullPointerException.class)
    public void testTranslateMethodWithNullInput2() {
        CharSequenceTranslator translator = new StandardTranslator();

        translator.translate(null, new OutputStreamWriter(new ByteArrayOutputStream()));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with IOException
     */
    @Test
    public void testTranslateMethodWithIOException() throws IOException {
        CharSequenceTranslator translator = new StandardTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("IOException for testing");
            }
        };

        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        String input = "Test String with special chars: ☠👻👽🌌";

        assertThrows(IOException.class, () -> translator.translate(input, 0, out));
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
     */
    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator1 = new StandardTranslator();
        CharSequenceTranslator translator2 = new StandardTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertEquals("org.apache.commons.lang3.text.translate.AggregateTranslator@",
                mergedTranslator.getClass().getName());
    }

    /**
     * Test method for {@link CharSequenceTranslator#hex(int)}
     */
    @Test
    public void testHexMethod() {
        assertEquals("415468657374616E7461626C6F77", CharSequenceTranslator.hex(0x65746865));
    }

}