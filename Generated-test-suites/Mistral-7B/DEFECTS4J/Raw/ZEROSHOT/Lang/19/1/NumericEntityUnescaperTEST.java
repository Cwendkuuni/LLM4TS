package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

import org.junit.Test;

/**
 * Tests for NumericEntityUnescaper class
 *
 * @author Johnson.D.Njui
 */
public class NumericEntityUnescaperTest {

    private static final String TEST_CHAR_SEQUENCE = "&amp; &lt; &gt; &apos; &quot; &#32; &#34; &#39; &#128; &#x7F; &#65535; &#xFFFF; &#x1F600;";

    @Test
    public void testTranslate() throws Exception {
        String testInput = "&amp; &lt; &gt; &apos; &quot; &#32; &#34; &#39; &#128; &#x7F; &#65535; &#xFFFF; &#x1F600;";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        for (int i = 0; i < TEST_CHAR_SEQUENCE.length(); i++) {
            int length = unescaper.translate(CharSequenceUtils.subSequence(testInput, i, TEST_CHAR_SEQUENCE.length()), i, writer);
            String actualOutput = outWriter.toString();
            String expectedOutput = new String(CharSequenceUtils.subSequence(TEST_CHAR_SEQUENCE, i, TEST_CHAR_SEQUENCE.length()).toString().getBytes(), 0, length);
            assertEquals(expectedOutput, actualOutput);
            i += length - 1;
            outWriter.getBuffer().setLength(0);
        }
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNegativeIndex() throws Exception {
        String testInput = "&amp; &lt; &gt; &apos; &quot; &#32; &#34; &#39; &#128; &#x7F; &#65535; &#xFFFF; &#x1F600;";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length()) * (-1);
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithInvalidEntity() throws Exception {
        String testInput = "&%&#";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateWithInvalidCharacterCode() throws Exception {
        String testInput = "&&#xxxxx;";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateWithInvalidOctalCode() throws Exception {
        String testInput = "&#0000";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateWithInvalidHexCode() throws Exception {
        String testInput = "&#xinvalid";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        String testInput = "";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
        assertEquals("", outWriter.toString());
    }

    @Test
    public void testTranslateWithNoEntityInInput() throws Exception {
        String testInput = "Hello World!";
        StringWriter outWriter = new StringWriter();
        Writer writer = new StringWriter(outWriter);

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = new Random().nextInt(TEST_CHAR_SEQUENCE.length());
        unescaper.translate(CharSequenceUtils.subSequence(testInput, index, TEST_CHAR_SEQUENCE.length()), index, writer);
        assertEquals("", outWriter.toString());
    }
}