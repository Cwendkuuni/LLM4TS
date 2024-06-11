package org.apache.commons.lang3.text.translate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private static final String TEST_INPUT = "&#65;&#x41;&#128581;&#38;&#x26;";

    private static NumericEntityUnescaper unescaper;

    @BeforeClass
    public static void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_typical() throws IOException {
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("A");
        expectedOut.write("A");
        expectedOut.write("😅");
        expectedOut.write("&");

        int translatedChars = unescaper.translate(TEST_INPUT, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(6, translatedChars);
    }

    @Test
    public void testTranslate_withSemicolon() throws IOException {
        String inputWithSemicolon = "&#65;&#x41;&#128581;&#38;&#x26;&#59;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("A");
        expectedOut.write("A");
        expectedOut.write("😅");
        expectedOut.write("&");

        int translatedChars = unescaper.translate(inputWithSemicolon, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(8, translatedChars);
    }

    @Test
    public void testTranslate_edgeCase_StartIndex() throws IOException {
        String inputWithExtraCharacters = "ExtraCharacters&#65;&#x41;&#128581;&#38;&#x26;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("ExtraCharacters");
        expectedOut.write("A");
        expectedOut.write("A");
        expectedOut.write("😅");
        expectedOut.write("&");

        int translatedChars = unescaper.translate(inputWithExtraCharacters, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(15, translatedChars);
    }

    @Test
    public void testTranslate_edgeCase_EndIndex() throws IOException {
        String inputWithExtraCharacters = "&#65;&#x41;&#128581;&#38;&#x26;ExtraCharacters";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("A");
        expectedOut.write("A");
        expectedOut.write("😅");
        expectedOut.write("&");
        expectedOut.write("ExtraCharacters");

        int translatedChars = unescaper.translate(inputWithExtraCharacters, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(6, translatedChars);
    }

    @Test
    public void testTranslate_hexadecimalInteger() throws IOException {
        String inputWithHexadecimal = "&#x41;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("A");

        int translatedChars = unescaper.translate(inputWithHexadecimal, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(7, translatedChars);
    }

    @Test
    public void testTranslate_decimalInteger() throws IOException {
        String inputWithDecimal = "&#65;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("A");

        int translatedChars = unescaper.translate(inputWithDecimal, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(6, translatedChars);
    }

    @Test
    public void testTranslate_highCodePoint() throws IOException {
        String inputWithHighCodePoint = "&#128581;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();
        expectedOut.write("😅");

        int translatedChars = unescaper.translate(inputWithHighCodePoint, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(10, translatedChars);
    }

    @Test
    public void testTranslate_invalidInteger() throws IOException {
        String inputWithInvalidInteger = "&#65A;";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();

        int translatedChars = unescaper.translate(inputWithInvalidInteger, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(0, translatedChars);
    }

    @Test
    public void testTranslate_incompleteNumericEntity() throws IOException {
        String inputWithIncompleteEntity = "&#[x41;]";
        StringWriter out = new StringWriter();
        Writer expectedOut = new StringWriter();

        int translatedChars = unescaper.translate(inputWithIncompleteEntity, 0, out);

        assertEquals(expectedOut.toString(), out.toString());
        assertEquals(0, translatedChars);
    }
}