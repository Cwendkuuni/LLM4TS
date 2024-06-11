package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--example";
        String str2 = "-example";
        String str3 = "example";
        String str4 = null;

        assertEquals("example", Util.stripLeadingHyphens(str1));
        assertEquals("example", Util.stripLeadingHyphens(str2));
        assertEquals("example", Util.stripLeadingHyphens(str3));
        assertNull(Util.stripLeadingHyphens(str4));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"example\"";
        String str2 = "example";
        String str3 = "\"";
        String str4 = "\"example";
        String str5 = "example\"";
        String str6 = null;

        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str2));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str3));
        assertEquals("\"example", Util.stripLeadingAndTrailingQuotes(str4));
        assertEquals("example\"", Util.stripLeadingAndTrailingQuotes(str5));
        assertNull(Util.stripLeadingAndTrailingQuotes(str6));
    }

}