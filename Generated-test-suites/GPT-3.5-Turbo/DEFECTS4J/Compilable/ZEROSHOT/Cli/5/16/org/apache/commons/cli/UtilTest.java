package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void stripLeadingHyphensTest() {
        // Test case 1: str starts with "--"
        String str1 = "--test";
        String expectedResult1 = "test";
        String result1 = Util.stripLeadingHyphens(str1);
        Assert.assertEquals(expectedResult1, result1);

        // Test case 2: str starts with "-"
        String str2 = "-test";
        String expectedResult2 = "test";
        String result2 = Util.stripLeadingHyphens(str2);
        Assert.assertEquals(expectedResult2, result2);

        // Test case 3: str starts with neither "--" nor "-"
        String str3 = "test";
        String expectedResult3 = "test";
        String result3 = Util.stripLeadingHyphens(str3);
        Assert.assertEquals(expectedResult3, result3);

        // Additional test case: str is empty
        String str4 = "";
        String expectedResult4 = "";
        String result4 = Util.stripLeadingHyphens(str4);
        Assert.assertEquals(expectedResult4, result4);
    }

    @Test
    public void stripLeadingAndTrailingQuotesTest() {
        // Test case 1: str starts and ends with quotes
        String str1 = "\"test\"";
        String expectedResult1 = "test";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        Assert.assertEquals(expectedResult1, result1);

        // Test case 2: str starts with quote
        String str2 = "\"test";
        String expectedResult2 = "test";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        Assert.assertEquals(expectedResult2, result2);

        // Test case 3: str ends with quote
        String str3 = "test\"";
        String expectedResult3 = "test";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        Assert.assertEquals(expectedResult3, result3);

        // Test case 4: str does not start or end with quotes
        String str4 = "test";
        String expectedResult4 = "test";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        Assert.assertEquals(expectedResult4, result4);

        // Additional test case: str is empty
        String str5 = "";
        String expectedResult5 = "";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        Assert.assertEquals(expectedResult5, result5);
    }
}
