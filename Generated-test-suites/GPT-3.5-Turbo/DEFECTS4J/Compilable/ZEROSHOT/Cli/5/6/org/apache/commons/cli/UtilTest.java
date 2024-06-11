package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";
        String str4 = "---abc---";

        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);
        String result4 = Util.stripLeadingHyphens(str4);

        assertEquals("test", result1);
        assertEquals("test", result2);
        assertEquals("test", result3);
        assertEquals("--abc---", result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "\"one two\"";
        String str3 = "no quotes";
        String str4 = "\"\"";

        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);

        assertEquals("test", result1);
        assertEquals("one two", result2);
        assertEquals("no quotes", result3);
        assertEquals("", result4);
    }
}