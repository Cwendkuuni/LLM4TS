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
        String expected = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "test";
        expected = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "";
        expected = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "--  ";
        expected = " ";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
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
        String input = "\"test one two\"";
        String expected = "test one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "test one two";
        expected = "test one two";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "";
        expected = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "\"  ";
        expected = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}