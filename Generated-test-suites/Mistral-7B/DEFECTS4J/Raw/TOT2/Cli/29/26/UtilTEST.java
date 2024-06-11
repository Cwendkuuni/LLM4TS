package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testNoLeadingHyphens() {
        String input = "test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMultipleLeadingHyphens() {
        String input = "--test--";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testMultipleLeadingTrailingQuotes() {
        String input = "\"\"test\"\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testInvalidQuotes() {
        String input = "\"\"'test\"\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSpecialCharacters() {
        String input = "test!@#$%^&*()_+|~{}\\:<>\"'";
        String expectedOutput = "test!@#$%^&*()_+|~{}\\:<>\'";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testNullInput() {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testNullInputStripQuotes() {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testUnprintableCharacters() {
        String input = "\u0000test";
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testUnprintableCharactersStripQuotes() {
        String input = "\u0000test";
        String expectedOutput = null;
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnhandledEdgeCases() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "   ";
        expectedOutput = "   ";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "  test  ";
        expectedOutput = " test ";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test   ";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test  ";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = " test ";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test--";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnhandledEdgeCasesStripQuotes() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = null;
        expectedOutput = null;
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "   ";
        expectedOutput = "   ";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "  test  ";
        expectedOutput = " test ";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test   ";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test  ";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = " test ";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"test\"--";
        expectedOutput = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}