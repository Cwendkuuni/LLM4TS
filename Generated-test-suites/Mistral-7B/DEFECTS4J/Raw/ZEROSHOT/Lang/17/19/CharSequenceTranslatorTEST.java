package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Tests for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} and {@link CharSequenceTranslator#translate(CharSequence)} methods.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new SimpleTranslator();
        String input = "\u0041\uD840\uDC25\u0042\u0043";
        String expectedOutput = "A&#x10400;&#xC025;BCD";

        StringWriter writer = new StringWriter();
        int consumed = translator.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
        String output = writer.toString();
        assertEquals(expectedOutput, output);
        assertEquals(input.length(), consumed);

        String outputTranslated = translator.translate(input);
        assertEquals(expectedOutput, outputTranslated);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     */
    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new SimpleTranslator();
        String input = "\u0041\uD840\uDC25\u0042\u0043";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
        String output = writer.toString();
        assertEquals("\u0041\u0041\uD840\uDC25\uD840\uDC25\u0042\u0042\u0043\u0043", input);
        assertEquals("A\u0041A\uD840\uDC25\uD840\uDC25B\u0042C\u0043D", output);
        assertEquals(input.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} and {@link CharSequenceTranslator#translate(CharSequence, Writer)} methods with AggregateTranslator.
     */
    @Test
    public void testTranslateWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new SimpleTranslator();
        CharSequenceTranslator translator2 = new SimpleTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int consumed = super.translate(input, index, out);
                if (consumed > 0) {
                    out.write("1");
                }
                return consumed;
            }
        };

        CharSequenceTranslator aggregatedTranslator = new AggregateTranslator(translator1, translator2);
        String input = "\u0041\u0041\uD840\uDC25\uD840\uDC25\u0042\u0042\u0043\u0043";
        StringWriter writer = new StringWriter();

        int consumed = aggregatedTranslator.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
        String output = writer.toString();
        assertEquals("\u0041\u0041\uD840\uDC25\uD840\uDC25\u0042\u0042\u0043\u0043", input);
        assertEquals("A\u0041A\uD840\uDC25\uD840\uDC25B\u0042C\u0043D1111", output);
        assertEquals(input.length() * 2, consumed);

        String outputTranslated = aggregatedTranslator.translate(input);
        assertEquals("\u0041\u0041\uD840\uDC25\uD840\uDC25\u0042\u0042\u0043\u0043", input);
        assertEquals("A\u0041A\uD840\uDC25\uD840\uDC25B\u0042C\u0043D1111", outputTranslated);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an empty input.
     */
    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(StringUtils.EMPTY, 0, writer);
        String output = writer.toString();
        assertEquals(StringUtils.EMPTY, output);
        assertEquals(0, consumed);
    }
}