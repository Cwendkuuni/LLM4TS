package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Test cases for {@link NumericEntityUnescaper} class.
 *
 * @author John Doe
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translate method with valid hex entity.
     */
    @Test
    public void testTranslateHexEntity() {
        String input = "&#[xA9]";
        String expectedOutput = "";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);
        String actualOutput = out.toString();

        assertEquals(expectedOutput, actualOutput.substring(0, length));
    }

    /**
     * Test translate method with valid decimal entity.
     */
    @Test
    public void testTranslateDecimalEntity() {
        String input = "&#[123]";
        String expectedOutput = "ᵃ";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);
        String actualOutput = out.toString();

        assertEquals(expectedOutput, actualOutput.substring(0, length));
    }

    /**
     * Test translate method with invalid entity.
     */
    @Test
    public void testTranslateInvalidEntity() {
        String input = "&[invalidEntity";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);

        assertEquals(0, length);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        String input = "";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);

        assertEquals(0, length);
    }

    /**
     * Test translate method with input that does not start with & and #.
     */
    @Test
    public void testTranslateInvalidInputStart() {
        String input = "invalidInput";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);

        assertEquals(0, length);
    }

    /**
     * Test translate method with invalid XML character at the start.
     */
    @Test
    public void testTranslateInvalidXmlStart() {
        String input = "&%#[xCA];";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);

        assertEquals(0, length);
    }

    /**
     * Test translate method with invalid XML character at the end.
     */
    @Test
    public void testTranslateInvalidXmlEnd() {
        String input = "&#[xCA];%";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        int length = unescaper.translate(new String(input), 0, out);

        assertEquals(0, length);
    }

    /**
     * Test translate method with valid XML entities in random order.
     */
    @Test
    public void testTranslateRandomEntities() throws UnsupportedEncodingException {
        String input = new String(new StringBuilder()
                .append("&#[xCA];&#[12];&#[x5C];&#[x41];&#[x26];&#[x60];&#[x23];")
                .toString(), "UTF-8");

        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence sequence = new String(input);
        int length = unescaper.translate(sequence, 0, out);

        String expectedOutput = new String(new StringBuilder()
                .append("Ẑ")
                .append((char) 12)
                .append("|")
                .append("A")
                .append("&")
                .append("`")
                .append("#")
                .toString());

        String actualOutput = out.toString().substring(0, length);

        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with valid XML entities that have different lengths.
     */
    @Test
    public void testTranslateEntitiesWithDifferentLengths() throws UnsupportedEncodingException {
        String input = new String(new StringBuilder()
                .append("&#[xCA];&#[12];&#[x5C];&#[x41];&#[x26];")
                .append("&#[x60];&#[x23];&#[x7F];&#[x78];")
                .toString(), "UTF-8");

        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence sequence = new String(input);
        int length = unescaper.translate(sequence, 0, out);

        String expectedOutput = new String(new StringBuilder()
                .append("Ẑ")
                .append((char) 12)
                .append("|")
                .append("A")
                .append("&")
                .append("`")
                .append("#")
                .append(".")
                .append("~")
                .toString());

        String actualOutput = out.toString().substring(0, length);

        assertEquals(expectedOutput, actualOutput);
    }
}