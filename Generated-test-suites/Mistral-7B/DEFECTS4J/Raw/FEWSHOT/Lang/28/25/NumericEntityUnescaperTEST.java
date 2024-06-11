package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = new StringWriter();
        CharSequence input = "&lt;p&gt;This is a &amp; special &apos;test&apos; with &#x26;#x61;#x6C;#x69;#x62;#x65;#x73;#x26;#x3D;#x31;&#xA0;&#x26;#x3C;#x26;#x61;#x63;#x74;#x69;#x6F;#x6E;#x26;#x27;#x26;#x3E;.";

        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(input, index, out);
            index += charsTranslated;
        }

        assertEquals("This is a special test with < p >xml</p>.", out.toString());
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "";

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = null;

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&blabla;";

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateEmptyEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&nbsp;";

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateSingleCharacterEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&A;";

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("A", out.toString());
        assertEquals(1, charsTranslated);
    }

    @Test
    public void testTranslateSingleCharacterEntityHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&#x41;";

        int index = 0;
        int charsTranslated = unescaper.translate(input, index, out);

        assertEquals("A", out.toString());
        assertEquals(1, charsTranslated);
    }

    @Test
    public void testTranslateMultipleCharacterEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&eacute;&#xA0;&#x26;#x61;#x63;#x74;#x69;#x6F;#x6E;#x26;#x3C;#x26;#x27;#x26;#x3E;.";

        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(input, index, out);
            index += charsTranslated;
        }

        assertEquals("é  xml</p>.", out.toString());
    }

    @Test
    public void testTranslateMultipleCharacterEntityHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        CharSequence input = "&#x65;#x6E;#x63;#x6F;#x64;#x65;#x64;#x20;#x3C;#x3E;#x26;#x3C;#x26;#x27;#x26;#x3E;.";

        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(input, index, out);
            index += chardsTranslated;
        }

        assertEquals("edifices</p>.", out.toString());
    }
}