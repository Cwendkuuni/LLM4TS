package org.apache.commons.cli;
import org.junit.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case - hyphens at the beginning of the string
        String str1 = "--Test";
        String result1 = Util.stripLeadingHyphens(str1);
        Assert.assertEquals("Test", result1);

        // Typical use case - one hyphen at the beginning of the string
        String str2 = "-Test";
        String result2 = Util.stripLeadingHyphens(str2);
        Assert.assertEquals("Test", result2);

        // Edge case - empty string
        String str3 = "";
        String result3 = Util.stripLeadingHyphens(str3);
        Assert.assertEquals("", result3);

        // Error scenario - null string
        String str4 = null;
        String result4 = Util.stripLeadingHyphens(str4);
        Assert.assertNull(result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case - leading and trailing quotes
        String str1 = "\"Test\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        Assert.assertEquals("Test", result1);

        // Typical use case - only leading quote
        String str2 = "\"Test";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        Assert.assertEquals("Test", result2);

        // Typical use case - only trailing quote
        String str3 = "Test\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        Assert.assertEquals("Test", result3);

        // Edge case - empty string
        String str4 = "";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        Assert.assertEquals("", result4);

        // Error scenario - null string
        String str5 = null;
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        Assert.assertNull(result5);
    }
}