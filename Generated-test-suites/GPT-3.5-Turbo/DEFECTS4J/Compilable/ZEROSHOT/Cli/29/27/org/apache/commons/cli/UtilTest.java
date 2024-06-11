package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        String str = null;
        assertNull(Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingHyphens_startsWithDoubleHyphen() {
        String str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingHyphens_startsWithSingleHyphen() {
        String str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingHyphens_noHyphen() {
        String str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithDoubleQuotes() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithDoubleQuotes() {
        String str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        String str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithSingleQuote() {
        String str = "'test'";
        assertEquals("'test'", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithSingleQuote() {
        String str = "test'";
        assertEquals("test'", Util.stripLeadingAndTrailingQuotes(str));
    }
}