package org.apache.commons.cli;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--testString";
        String result1 = Util.stripLeadingHyphens(str1);
        assert(result1.equals("testString"));

        // Edge case: empty string
        String str2 = "";
        String result2 = Util.stripLeadingHyphens(str2);
        assert(result2.equals(""));

        // Edge case: string with only hyphens
        String str3 = "--";
        String result3 = Util.stripLeadingHyphens(str3);
        assert(result3.equals(""));

        // Error scenario: string without hyphens
        String str4 = "testString";
        String result4 = Util.stripLeadingHyphens(str4);
        assert(result4.equals("testString"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"testString\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assert(result1.equals("testString"));

        // Edge case: empty string
        String str2 = "";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assert(result2.equals(""));

        // Edge case: string with only leading quotes
        String str3 = "\"testString";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assert(result3.equals("testString"));

        // Edge case: string with only trailing quotes
        String str4 = "testString\"";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assert(result4.equals("testString"));

        // Error scenario: string without quotes
        String str5 = "testString";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assert(result5.equals("testString"));

        // Error scenario: string with unmatched quotes
        String str6 = "\"testString";
        String result6 = Util.stripLeadingAndTrailingQuotes(str6);
        assert(result6.equals("\"testString"));

        // Error scenario: string with unmatched quotes
        String str7 = "testString\"";
        String result7 = Util.stripLeadingAndTrailingQuotes(str7);
        assert(result7.equals("testString\""));
    }
}