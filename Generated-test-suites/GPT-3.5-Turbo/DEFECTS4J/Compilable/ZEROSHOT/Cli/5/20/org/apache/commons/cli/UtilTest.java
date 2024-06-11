package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test for hyphen at the beginning
        String str1 = "--test";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("test", result1);

        // Test for hyphen in the middle
        String str2 = "te-st";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("te-st", result2);

        // Test for hyphen at the end
        String str3 = "test-";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("test-", result3);

        // Test for no hyphen
        String str4 = "noHyphen";
        String result4 = Util.stripLeadingHyphens(str4);
        assertEquals("noHyphen", result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test for leading and trailing quotes
        String str1 = "\"one two\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("one two", result1);

        // Test for leading quote only
        String str2 = "\"one two";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("one two", result2);

        // Test for trailing quote only
        String str3 = "one two\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("one two", result3);

        // Test for no quotes
        String str4 = "noQuotes";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("noQuotes", result4);
    }

}