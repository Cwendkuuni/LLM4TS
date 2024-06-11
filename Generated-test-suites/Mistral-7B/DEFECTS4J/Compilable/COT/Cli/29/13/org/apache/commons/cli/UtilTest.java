package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(input, output);

        input = "---test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, output);

        input = "\"test \"";
        expectedOutput = "test ";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\" test\"";
        expectedOutput = " test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, output);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}