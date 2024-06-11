package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with leading "--"
        String str1 = Util.stripLeadingHyphens("--test");
        assertEquals("test", str1);

        // Test with leading "-"
        String str2 = Util.stripLeadingHyphens("-test");
        assertEquals("test", str2);

        // Test with no leading hyphens
        String str3 = Util.stripLeadingHyphens("test");
        assertEquals("test", str3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with leading and trailing quotes
        String str1 = Util.stripLeadingAndTrailingQuotes("\"one two\"");
        assertEquals("one two", str1);

        // Test with leading quote
        String str2 = Util.stripLeadingAndTrailingQuotes("\"one two");
        assertEquals("one two", str2);

        // Test with trailing quote
        String str3 = Util.stripLeadingAndTrailingQuotes("one two\"");
        assertEquals("one two", str3);

        // Test with no quotes
        String str4 = Util.stripLeadingAndTrailingQuotes("one two");
        assertEquals("one two", str4);
    }
}

