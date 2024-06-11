package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = Util.stripLeadingHyphens("--test");
        assertEquals("test", str);

        str = Util.stripLeadingHyphens("-test");
        assertEquals("test", str);

        str = Util.stripLeadingHyphens("test");
        assertEquals("test", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", str);

        str = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", str);
    }

    @Test
    public void testStripLeadingHyphensWithNull() {
        String str = Util.stripLeadingHyphens(null);
        assertNull(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String str = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("\"\"");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithMultipleQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("\"test\"\"");
        assertEquals("test\"", str);
    }
}