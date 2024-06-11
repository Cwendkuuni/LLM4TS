import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str1 = "--foo";
        String str2 = "foo";
        String str3 = "foo";
        String str4 = "";

        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);
        String result4 = Util.stripLeadingHyphens(str4);

        Assert.assertEquals("foo", result1);
        Assert.assertEquals("foo", result2);
        Assert.assertEquals("foo", result3);
        Assert.assertNull(result4);

        String str5 = "--foo--bar";
        String result5 = Util.stripLeadingHyphens(str5);
        Assert.assertEquals("foo--bar", result5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str1 = "\"foo bar\"";
        String str2 = "foo bar";
        String str3 = "\"foo bar\"";
        String str4 = "foo bar";
        String str5 = "";

        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);

        Assert.assertEquals("foo bar", result1);
        Assert.assertEquals("foo bar", result2);
        Assert.assertEquals("foo bar", result3);
        Assert.assertEquals("foo bar", result4);
        Assert.assertNull(result5);

        String str6 = "\"foo bar \n baz\"";
        String result6 = Util.stripLeadingAndTrailingQuotes(str6);
        Assert.assertEquals("foo bar\nbaz", result6);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingHyphens()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingAndTrailingQuotes()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }

    @Test
    public void testEmptyStringForStripLeadingHyphens()
    {
        String str = "";
        String result = Util.stripLeadingHyphens(str);
        Assert.assertEquals("", result);
    }

    @Test
    public void testEmptyStringForStripLeadingAndTrailingQuotes()
    {
        String str = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("", result);
    }
}