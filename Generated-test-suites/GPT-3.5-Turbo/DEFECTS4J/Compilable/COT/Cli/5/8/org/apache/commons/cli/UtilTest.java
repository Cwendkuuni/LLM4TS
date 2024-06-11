package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with str starting with "--"
        String str1 = "--test";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("test", result1);

        // Test with str starting with "-"
        String str2 = "-test";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("test", result2);

        // Test with str not starting with "-"
        String str3 = "test";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("test", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with str starting and ending with quotes
        String str1 = "\"test\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("test", result1);

        // Test with str starting with quote
        String str2 = "\"test";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("test", result2);
        
        // Test with str ending with quote
        String str3 = "test\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("test", result3);
        
        // Test with str not starting or ending with quotes
        String str4 = "test";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("test", result4);
    }

}