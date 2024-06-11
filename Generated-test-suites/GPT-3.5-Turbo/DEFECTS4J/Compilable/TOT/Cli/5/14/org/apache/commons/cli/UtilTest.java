package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical Use Case: String starts with "--"
        String str1 = "--example";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("example", result1);

        // Typical Use Case: String starts with "-"
        String str2 = "-example";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("example", result2);

        // Edge Case: Empty String
        String str3 = "";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("", result3);

        // Error Scenario: String does not start with "-" or "--"
        String str4 = "example";
        String result4 = Util.stripLeadingHyphens(str4);
        assertEquals("example", result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical Use Case: String starts and ends with quotes
        String str1 = "\"example\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("example", result1);

        // Typical Use Case: String starts with quote
        String str2 = "\"example";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("\"example", result2);

        // Typical Use Case: String ends with quote
        String str3 = "example\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("example\"", result3);

        // Edge Case: Empty String
        String str4 = "";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("", result4);

        // Error Scenario: String starts and ends with different quotes
        String str5 = "'example\"";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assertEquals("'example\"", result5);
    }
}