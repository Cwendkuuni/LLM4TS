package org.apache.commons.cli.test;

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
        String testString1 = "--test";
        String expectedResult1 = "test";
        String result1 = Util.stripLeadingHyphens(testString1);
        Assert.assertEquals(expectedResult1, result1);

        String testString2 = "test";
        String expectedResult2 = "test";
        String result2 = Util.stripLeadingHyphens(testString2);
        Assert.assertEquals(expectedResult2, result2);

        String testString3 = "---test";
        String expectedResult3 = "test";
        String result3 = Util.stripLeadingHyphens(testString3);
        Assert.assertEquals(expectedResult3, result3);

        String testString4 = "";
        String expectedResult4 = "";
        String result4 = Util.stripLeadingHyphens(testString4);
        Assert.assertEquals(expectedResult4, result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String testString1 = "";
        String expectedResult1 = "";
        String result1 = Util.stripLeadingAndTrailingQuotes(testString1);
        Assert.assertEquals(expectedResult1, result1);

        String testString2 = "\"test\"";
        String expectedResult2 = "test";
        String result2 = Util.stripLeadingAndTrailingQuotes(testString2);
        Assert.assertEquals(expectedResult2, result2);

        String testString3 = "\"test1 test2\"";
        String expectedResult3 = "test1 test2";
        String result3 = Util.stripLeadingAndTrailingQuotes(testString3);
        Assert.assertEquals(expectedResult3, result3);

        String testString4 = "\"\"";
        String expectedResult4 = "";
        String result4 = Util.stripLeadingAndTrailingQuotes(testString4);
        Assert.assertEquals(expectedResult4, result4);

        String testString5 = "\"test\"\n";
        String expectedResult5 = "test";
        String result5 = Util.stripLeadingAndTrailingQuotes(testString5);
        Assert.assertEquals(expectedResult5, result5);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringForStripLeadingAndTrailingQuotes()
    {
        String testString = null;
        Util.stripLeadingAndTrailingQuotes(testString);
    }
}