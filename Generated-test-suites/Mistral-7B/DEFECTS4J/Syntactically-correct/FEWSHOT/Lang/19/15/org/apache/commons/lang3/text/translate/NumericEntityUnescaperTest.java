package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp; &lt; &gt; &apos; &quot; &#34; &#x2019; &#128512; &#xFFFD; &#x1F600; &#x2603;";
        StringWriter outWriter = new StringWriter();

        int index = 0;
        int length = input.length();
        while (index < length) {
            int charsTranslated = unescaper.translate(new StringCharSequence(input), index, outWriter);
            index += charsTranslated;
        }

        String expectedOutput = "&amp; &lt; &gt; ' ' \" \" \" \" ‘ 🌿 � 😱 🪙";
        assertEquals(expectedOutput, outWriter.toString());
    }

    @Test
    public void testTranslateWithSingleCharacterEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&eacute; &nbspp; &iexcl; &OElig; &Agrave;";
        StringWriter outWriter = new StringWriter();

        int index = 0;
        int length = input.length();
        while (index < length) {
            int charsTranslated = unescaper.translate(new StringCharSequence(input), index, outWriter);
            index += charsTranslated;
        }

        String expectedOutput = "é \u00A0 ! Ö A";
        assertEquals(expectedOutput, outWriter.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter outWriter = new StringWriter();
        int charsTranslated = unescaper.translate(new StringCharSequence(""), 0, outWriter);
        assertEquals(0, charsTranslated);
        assertEquals("", outWriter.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter outWriter = new StringWriter();
        int charsTranslated = unescaper.translate(null, 0, outWriter);
        assertNull(outWriter.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&invalidEntity;";
        StringWriter outWriter = new StringWriter();

        int index = 0;
        int length = input.length();
        try {
            int charsTranslated = unescaper.translate(new StringCharSequence(input), index, outWriter);
            fail("Expected NumberFormatException");
        } catch (IOException e) {
            assertEquals("FAIL: invalidEntity[0][9]", e.getMessage());
        }

        assertEquals("", outWriter.toString());
        assertEquals(0, charsTranslated);
    }

    @Test
    public void testTranslateWithHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&#x41; &#x1F60A; &#x1F60B; &#x1F60C;";
        StringWriter outWriter = new StringWriter();

        int index = 0;
        int length = input.length();
        while (index < length) {
            int charsTranslated = unescaper.translate(new StringCharSequence(input), index, outWriter);
            index += charsTranslated;
        }

        String expectedOutput = "A 🦊 🦋 🦌";
        assertEquals(expectedOutput, outWriter.toString());
    }

    @Test
    public void testTranslateWithLargeEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Random random = new Random();
        int entityValue = random.nextInt(0x10FFFF) + 1;
        String input = "&#" + Integer.toHexString(entityValue) + ";";
        StringWriter outWriter = new StringWriter();

        int index = 0;
        int length = input.length();
        while (index < length) {
            int charsTranslated = unescaper.translate(new StringCharSequence(input), index, outWriter);
            index += charsTranslated;
        }

        char[] chars = Character.toChars(entityValue);
        String expectedOutput = "" + chars[0] + chars[1];
        assertArrayEquals(new char[] { chars[0], chars[1] }, outWriter.toString().toCharArray());
    }
}