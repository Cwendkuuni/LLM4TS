package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithValidEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        // Testing with valid entities
        String input1 = "&amp; &lt; &gt; &apos; &quot; &eacute; &Aring; &aelig; &ndash; &mdash; &cent; &pound; &yen; &euro; &copy; &reg; &trade; &plusmn; &sup2; &sup3; &times; &divide; &iquest; &agrave; &aacute; &acirc; &atilde; &aring; &auml; &ccedil; &egrave; &eacute; &ecirc; &euml; &igrave; &Icirc; &iuml; &eth; &ntilde; &ograve; &oacute; &ocirc; &otilde; &ouml; &ouml; &ozeli; &szlig; &agrave; &auml; &aring; &eacute; &egrave; &eacute; &ecirc; &euml; &igrave; &Icirc; &iuml; &eth; &ntilde; &ouml; &ouml; &ouml; &szlig; &apos; &quot; &amp; &lt; &gt;";
        int index1 = 0;

        int charsWritten = 0;
        while (index1 < input1.length()) {
            int codePoint = unescaper.translate(new String(input1.substring(index1).getBytes("UTF-8")), index1, out);
            index1 += codePoint;
            charsWritten += codePoint;
        }

        String expectedOutput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&eacute;&Aring;&aelig;&ndash;&mdash;&cent;&pound;&yen;&euro;&copy;&reg;&trade;&plusmn;&sup2;&sup3;&times;&divide;&iquest;&agrave;&aacute;&acirc;&atilde;&aring;&auml;&ccedil;&egrave;&eacute;&ecirc;&euml;&igrave;&Icirc;&iuml;&eth;&ntilde;&ograve;&oacute;&ocirc;&otilde;&ouml;&ouml;&ouml;&szlig;&agrave;&auml;&aring;&eacute;&egrave&eacute;&ecirc;&euml;&igrave;&Icirc;&iuml&\u00E5\u00E4\u00E7\u00F6\u00F6\u00F8\u00F6\u00DC\u00E4\u00F6\u00FC\u00E4\u00F6\u00FC\u00F6\u00FC\u00E6";
        assertEquals(expectedOutput, out.toString());
        assertEquals(charsWritten, expectedOutput.length());
    }

    @Test
    public void testTranslateWithValidHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input1 = "&#x0061; &#x0062; &#x0063;";
        int index1 = 0;

        int charsWritten = 0;
        while (index1 < input1.length()) {
            int codePoint = unescaper.translate(new String(input1.substring(index1).getBytes("UTF-8")), index1, out);
            index1 += codePoint;
            charsWritten += codePoint;
        }

        String expectedOutput = "abc";
        assertEquals(expectedOutput, out.toString());
        assertEquals(charsWritten, expectedOutput.length());
    }

    @Test
    public void testTranslateWithInvalidEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input1 = "&abc; &#; &#123456789012345; &#x123456789012345x; &#x123456789012345z; &#x123456789012345";
        int index1 = 0;

        int charsWritten = 0;
        while (index1 < input1.length()) {
            int codePoint = unescaper.translate(new String(input1.substring(index1).getBytes("UTF-8")), index1, out);
            index1 += codePoint;
            charsWritten += codePoint;
        }

        assertEquals(0, charsWritten);
        assertEquals("FAIL: &abc;[0][5] FAIL: &#;[0][1] FAIL: &#123456789012345;[0][12] FAIL: &#x123456789012345x;[0][13] FAIL: &#x123456789012345z;[0][15] FAIL: &#x123456789012345;[0][16]", out.toString());
    }

    @Test
    public void testTranslateWithInvalidHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input1 = "&#x00Z; &#x00ff; &#x00FF00; &#x123456789012345x; &#x123456789012345z; &#x123456789012345";
        int index1 = 0;

        int charsWritten = 0;
        while (index1 < input1.length()) {
            int codePoint = unescaper.translate(new String(input1.substring(index1).getBytes("UTF-8")), index1, out);
            index1 += codePoint;
            charsWritten += codePoint;
        }

        assertEquals(0, charsWritten);
        assertEquals("FAIL: &#x00Z;[0][3] FAIL: &#x00ff;[0][6] FAIL: &#x00FF00;[0][7..10] FAIL: &#x123456789012345x;[0][11] FAIL: &#x123456789012345z;[0][13] FAIL: &#x123456789012345;[0][14]", out.toString());
    }

    @Test
    public void testTranslateWithEmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        String input1 = "";
        int index1 = 0;

        int charsWritten = unescaper.translate(input1, index1, out);
        assertEquals(0, charsWritten);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithNullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> unescaper.translate(new String(""), 0, null));
    }

    @Test
    public void testTranslateWithNullCharSequence() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, new StringBuilder()));
    }

    @Test
    public void testTranslateWithNegativeIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input1 = "abcdef";
        Writer out = new StringBuilder();

        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(input1, -1, out));
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(input1, Integer.MIN_VALUE, out));
    }
}