package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = Util.stripLeadingHyphens("--test");
        assertEquals("test", str1);

        String str2 = Util.stripLeadingHyphens("-test");
        assertEquals("test", str2);

        String str3 = Util.stripLeadingHyphens("test");
        assertEquals("test", str3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", str1);

        String str2 = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", str2);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = Util.stripLeadingHyphens("");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", str);
    }
}

