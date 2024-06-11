package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical Use Case: str starts with "--"
        String str1 = "--example";
        String expectedResult1 = "example";
        String actualResult1 = Util.stripLeadingHyphens(str1);
        Assert.assertEquals(expectedResult1, actualResult1);

        // Typical Use Case: str starts with "-"
        String str2 = "-hello";
        String expectedResult2 = "hello";
        String actualResult2 = Util.stripLeadingHyphens(str2);
        Assert.assertEquals(expectedResult2, actualResult2);

        // Edge Case: str does not start with "-" or "--"
        String str3 = "world";
        String expectedResult3 = "world";
        String actualResult3 = Util.stripLeadingHyphens(str3);
        Assert.assertEquals(expectedResult3, actualResult3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical Use Case: str starts and ends with quotes
        String str1 = "\"example\"";
        String expectedResult1 = "example";
        String actualResult1 = Util.stripLeadingAndTrailingQuotes(str1);
        Assert.assertEquals(expectedResult1, actualResult1);

        // Typical Use Case: str does not start or end with quotes
        String str2 = "hello";
        String expectedResult2 = "hello";
        String actualResult2 = Util.stripLeadingAndTrailingQuotes(str2);
        Assert.assertEquals(expectedResult2, actualResult2);

        // Edge Case: str starts with quote but ends without quote
        String str3 = "\"world";
        String expectedResult3 = "\"world";
        String actualResult3 = Util.stripLeadingAndTrailingQuotes(str3);
        Assert.assertEquals(expectedResult3, actualResult3);

        // Edge Case: str starts without quote but ends with quote
        String str4 = "example\"";
        String expectedResult4 = "example\"";
        String actualResult4 = Util.stripLeadingAndTrailingQuotes(str4);
        Assert.assertEquals(expectedResult4, actualResult4);
    }
}