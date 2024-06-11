package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str));

        String str2 = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str2));

        String str3 = "no-hyphen";
        assertEquals("no-hyphen", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"quoted\"";
        assertEquals("quoted", Util.stripLeadingAndTrailingQuotes(str));

        String str2 = "\"quote at beginning";
        assertEquals("quote at beginning", Util.stripLeadingAndTrailingQuotes(str2));

        String str3 = "quote at end\"";
        assertEquals("quote at end", Util.stripLeadingAndTrailingQuotes(str3));

        String str4 = "no quotes";
        assertEquals("no quotes", Util.stripLeadingAndTrailingQuotes(str4));
    }
}