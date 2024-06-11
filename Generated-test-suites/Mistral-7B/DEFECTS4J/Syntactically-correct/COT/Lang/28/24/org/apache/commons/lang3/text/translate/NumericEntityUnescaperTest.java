package org.apache.commons.lang3.text.translate;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws IOException, UnsupportedEncodingException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Basic functionality test
        String testString = "&amp;&#32;&#13;&#38;#x27;&#8226;&#8203;&#157;&#8211;&#8217;&#8222;&#169;&#160;&#161;&#162;&#163;&#164;&#165;&#166;&#167;&#168;&#169;&#170;&#171;&#172;&#173;&#174;&#175;&#176;&#177;&#178;&#179;&#x201C;&#x201D;";
        char[] input = testString.toCharArray();
        int index = 0;
        int length = input.length;

        for (; index < length; index++) {
            int charsWritten = unescaper.translate(new String(input, 0, index), index, writer);
            String expected = Character.isDefinedChars(input[index]) ? String.valueOf(input[index]) : "?";
            String actual = new String(writer.getBuffer(), 0, writer.getBuffer().length());
            writer.getBuffer().reset();
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with an empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String emptyInput = "";
        int index = 0;

        int charsWritten = unescaper.translate(new String(emptyInput), index, writer);
        Assert.assertEquals(0, charsWritten);
        Assert.assertEquals("", writer.getBuffer().toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with an input containing no entities.
     */
    @Test
    public void testTranslateNoEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String noEntitiesInput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;

        int charsWritten = unescaper.translate(new String(noEntitiesInput), index, writer);
        Assert.assertEquals(noEntitiesInput.length(), charsWritten);
        Assert.assertEquals(noEntitiesInput, writer.getBuffer().toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with an input containing entities out of order.
     */
    @Test
    public void testTranslateOutOfOrderEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String outOfOrderInput = "abc&def&123g&456h&x789&i&j&k&l&m&n&o&p";
        int index = 0;

        int charsWritten = unescaper.translate(new String(outOfOrderInput), index, writer);
        String expected = "abcdef123g456hx789ijklmnop";
        String actual = new String(writer.getBuffer(), 0, writer.getBuffer().length());
        writer.getBuffer().reset();
        Assert.assertEquals(expected.length(), charsWritten);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hex entities.
     */
    @Test
    public void testTranslateHexEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String hexEntitiesInput = "abc&amp;#x61;&#x62;&#x63;&#x64;&#x65;&#x66;&#x67;&#x68;&#x69;&#x6a;&#x6b;&#x6c;&#x6d;&#x6e;&#x6f;&#x70;&#x71;&#x72;&#x73;&#x74;&#x75;&#x76;&#x77;&#x78;&#x79;&#x7a;&#x7b;&#x7c;&#x7d;&#x7e;&#x7f;&#x80;&#x81;&#x82;&#x83;&#x84;&#x85;&#x86;&#x87;&#x88;&#x89;&#x8a;&#x8b;&#x8c;&#x8d;&#x8e;&#x8f;&#x90;&#x91;&#x92;&#x93;&#x94;&#x95;&#x96;&#x97;&#x98;&#x99;&#x9a;&#x9b;&#x9c;&#x9d;&#x9e;&#x9f;&#xa0;&#xa1;&#xa2;&#xa3;&#xa4;&#xa5;&#xa6;&#xa7;&#xa8;&#xa9;&#xaa;&#xab;&#xac;&#xad;&#xae;&#xaf;&#xb0;&#xb1;&#xb2;&#xb3;&#xb4;&#xb5;&#xb6;&#xb7;&#xb8;&#xb9;&#xba;&#xbb;&#xbc;&#xbd;&#xbe;&#xbf;&#xc0;&#xc1;&#xc2;&#xc3;&#xc4;&#xc5;&#xc6;&#xc7;&#xc8;&#xc9;&#xca;&#xcb;&#xcc;&#xcd;&#xce;&#xcf;&#xd0;&#xd1;&#xd2;&#xd3;&#xd4;&#xd5;&#xd6;&#xd7;&#xd8;&#xd9;&#xda;&#xdb;&#xdc;&#xdd;&#xde;&#xdf;&#xe0;&#xe1;&#xe2;&#xe3;&#xe4;&#xe5;&#xe6;&#xe7;&#xe8;&#xe9;&#xea;&#xeb;&#xec;&#xed;&#xee;&#xef;&#xf0;&#xf1;&#xf2;&#xf3;&#xf4;&#xf5;&#xf6;&#xf7;&#xf8;&#xf9;&#xfa;&#xfb;&#xfc;&#xfd;&#xfe;&#xff";
        int index = 0;

        int charsWritten = unescaper.translate(new String(hexEntitiesInput), index, writer);
        String expected = "abc＆４１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰ";
        String actual = new String(writer.getBuffer(), 0, writer.getBuffer().length());
        writer.getBuffer().reset();
        Assert.assertEquals(expected.length(), charsWritten);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid entities.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String invalidEntitiesInput = "abc&amp;&#xabcd;&#12345;";
        int index = 0;

        unescaper.translate(new String(invalidEntitiesInput), index, writer);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid hex entities.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String invalidHexEntitiesInput = "abc&amp;#xabcdx;";
        int index = 0;

        unescaper.translate(new String(invalidHexEntitiesInput), index, writer);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entities outside the input sequence.
     */
    @Test
    public void testTranslateEntitiesOutsideInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String entitiesOutsideInput = "abc&def&ghi";
        int index = "abc&def".length();

        int charsWritten = unescaper.translate(new String(entitiesOutsideInput.toCharArray(), 0, index), index, writer);
        Assert.assertEquals(0, charsWritten);
        Assert.assertEquals("", writer.getBuffer().toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entities at the beginning of the input sequence.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateEntitiesAtBeginning() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String entitiesAtBeginningInput = "&abcdef";
        int index = 0;

        unescaper.translate(new String(entitiesAtBeginningInput), index, writer);
    }
}