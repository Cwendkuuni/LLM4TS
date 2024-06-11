import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with string starting with two hyphens
        String str1 = "--test";
        String expected1 = "test";
        String actual1 = Util.stripLeadingHyphens(str1);
        assertEquals(expected1, actual1);

        // Test with string starting with one hyphen
        String str2 = "-test";
        String expected2 = "test";
        String actual2 = Util.stripLeadingHyphens(str2);
        assertEquals(expected2, actual2);

        // Test with string starting with no hyphen
        String str3 = "test";
        String expected3 = "test";
        String actual3 = Util.stripLeadingHyphens(str3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with string starting and ending with quotes
        String str1 = "\"test\"";
        String expected1 = "test";
        String actual1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals(expected1, actual1);

        // Test with string starting with quote
        String str2 = "\"test";
        String expected2 = "\"test";
        String actual2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals(expected2, actual2);

        // Test with string ending with quote
        String str3 = "test\"";
        String expected3 = "test\"";
        String actual3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals(expected3, actual3);

        // Test with string having no quotes
        String str4 = "test";
        String expected4 = "test";
        String actual4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals(expected4, actual4);
    }
}