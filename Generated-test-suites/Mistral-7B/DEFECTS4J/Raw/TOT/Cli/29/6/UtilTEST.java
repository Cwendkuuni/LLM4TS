package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Util class.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test-string";
        String result = Util.stripLeadingHyphens(str);
        Assert.assertEquals("test-string", result);

        str = "test-string";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals("test-string", result);

        str = "";
        result = Util.stripLeadingHyphens(str);
        Assert.assertNull(result);

        str = "--";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals("", result);

        str = "--test";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"test-string\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("test-string", result);

        str = "test-string";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("test-string", result);

        str = "\"\"";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("", result);

        str = "\"test\"";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("test", result);
    }
}