package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslateBasic() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&num;abcd &#x41;ef &#x03B4;g &#36;h";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        String expected = "\u006E\u0065\u0063\u0068\u0061;\u0065\u006e\u0075\u006E\u0074\u0068\u0065;\u0003\u03B4;\u0024";

        assertEquals(expected, writer.toString());
        assertEquals(expected.length(), actual);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty input
     */
    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with null input
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = null;
        int index = 0;

        unescaper.translate(new StringReader(source), index, writer);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid entity
     */
    @Test
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&invalidEntity;abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity with more than 8 digits
     */
    @Test
    public void testTranslateLongEntityHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        Random random = new Random();
        int entityLength = random.nextInt(13) + 8;
        String source = "&#[x" + new String(new char[entityLength]).replace("\u0000", "A") + ";abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity with more than 4 digits
     */
    @Test
    public void testTranslateLongEntityDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        Random random = new Random();
        int entityLength = random.nextInt(5) + 5;
        String source = "&#[3" + new String(new char[entityLength]).replace("\u0000", "0") + ";abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hex entity with letter before digits
     */
    @Test
    public void testTranslateInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&#[xA3B4;abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hex entity without # before x
     */
    @Test
    public void testTranslateMissingHexPrefix() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&x41;abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with decimal entity without # before number
     */
    @Test
    public void testTranslateMissingDecimalPrefix() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&341;abcd";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(0, actual);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity with semicolon inside
     */
    @Test
    public void testTranslateEntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String source = "&num;abcd;&#41;";
        int index = 0;

        int actual = unescaper.translate(new StringReader(source), index, writer);
        assertEquals(1, actual);
        assertEquals("\u006E\u0065\u0063\u0068\u0061;", writer.toString());
    }
}