package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_SimpleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&amp; &lt; &gt; &apos; &quot; &#128; &#x41; &#x00A9;";
        int index = 0;
        int length = input.length();

        while (index < length) {
            int translationLength = unescaper.translate(new String(input, index, length), index, out);
            String output = out.getBuffer().toString();
            out = new StringWriter(); // Reset for next iteration

            String expected = "&amp; &lt; &gt; &apos; &quot; &#128; &#x0041; &#xA9;";
            assertEquals(expected.substring(0, index + translationLength), output.substring(0, index + translationLength));
            index += translationLength;
        }

        assertEquals("", out.getBuffer().toString());
    }

    @Test
    public void testTranslate_SimpleEntities_Hex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&amp; &lt; &gt; &apos; &quot; &#x3C3; &#x41#x23;";
        int index = 0;
        int length = input.length();

        while (index < length) {
            int translationLength = unescaper.translate(new String(input, index, length), index, out);
            String output = out.getBuffer().toString();
            out = new StringWriter(); // Reset for next iteration

            String expected = "&amp; &lt; &gt; &apos; &quot; &#x3C3; &#x0041;&#x0023;";
            assertEquals(expected.substring(0, index + translationLength), output.substring(0, index + translationLength));
            index += translationLength;
        }

        assertEquals("", out.getBuffer().toString());
    }

    @Test
    public void testTranslate_InvalidEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&foobar &xfoobar &x &#xfoobar &% &\\";
        int index = 0;
        int length = input.length();

        while (index < length) {
            int translationLength = unescaper.translate(new String(input, index, length), index, out);
            String output = out.getBuffer().toString();
            out = new StringWriter(); // Reset for next iteration

            assertEquals("", output.substring(0, index + translationLength));
            index += translationLength;
        }

        assertEquals("", out.getBuffer().toString());
    }

    @Test
    public void testTranslate_MultibyteEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&#xFFFD;";
        int index = 0;
        int length = input.length();

        int translationLength = unescaper.translate(new String(input, index, length), index, out);
        String output = out.getBuffer().toString();
        out = new StringWriter(); // Reset for next iteration

        assertArrayEquals(Character.toChars(0xFFFD), output.toCharArray());

        assertEquals("", output.substring(0, index + translationLength));
        index += translationLength;
    }

    @Test
    public void testTranslate_InvalidMultibyteEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&#x110000;";
        int index = 0;
        int length = input.length();

        int translationLength = unescaper.translate(new String(input, index, length), index, out);
        String output = out.getBuffer().toString();
        out = new StringWriter(); // Reset for next iteration

        assertNull(output);

        assertEquals("", output.substring(0, index + translationLength));
        index += translationLength;
    }
}