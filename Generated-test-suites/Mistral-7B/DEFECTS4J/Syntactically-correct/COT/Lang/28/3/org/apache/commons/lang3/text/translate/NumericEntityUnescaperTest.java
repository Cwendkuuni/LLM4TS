package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    private final String testInput = "&amp;&#x26;#xa9;&#112;&#x7f;&#x303;&#136;&#141;";
    private final String expectedOutput = "&amp;&& Å°¡bítá";

    @org.junit.Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return unescaper.translate(input, index, out);
            }
        };

        int translatedLength = translator.translate(new StringReader(testInput), 0, out);
        assertEquals(expectedOutput, out.toString().substring(0, translatedLength));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty input.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testTranslateEmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return unescaper.translate(input, index, out);
            }
        };

        int translatedLength = translator.translate(CharSequence.class.newInstance(), 0, out);
        assertEquals(0, translatedLength);
        assertEquals("", out.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid entity.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testTranslateInvalidEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return unescaper.translate(input, index, out);
            }
        };

        String testInput = "&badEntity;";
        int translatedLength = translator.translate(new StringReader(testInput), 0, out);
        assertEquals(0, translatedLength);
        assertEquals("", out.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity outside input sequence.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testTranslateEntityOutsideInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return unescaper.translate(input, index, out);
            }
        };

        String testInput = "&amp;";
        int index = 1;
        int translatedLength = translator.translate(new StringReader(testInput), index, out);
        assertEquals(0, translatedLength);
        assertEquals("", out.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hex entity starting with letter X.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testTranslateHexEntityStartsWithX() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return unescaper.translate(input, index, out);
            }
        };

        String testInput = "&x#hexValue;";
        int index = 2;
        int hexValue = 0x123;
        int translatedLength = translator.translate(new StringReader(testInput), index, out);
        assertEquals(Character.toString((char) hexValue.byteValue()), out.toString());
        assertEquals(1 + 2 + 2, translatedLength);
    }
}