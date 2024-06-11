import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--str";
        String expected = "str";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, output);

        input = "-str";
        expected = "str";
        output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, output);

        input = "str";
        expected = "str";
        output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, output);

        input = null;
        expected = null;
        output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithIllegalArgument()
    {
        String input = "";
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"str1 str2\"";
        String expected = "str1 str2";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, output);

        input = "\"str\"";
        expected = "str";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, output);

        input = "str";
        expected = "str";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, output);

        input = null;
        expected = null;
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, output);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithStringIndexOutOfBoundsException()
    {
        String input = "\"str\"str";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}