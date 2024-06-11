package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullInput() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_doubleHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
    }

    @Test
    public void testStripLeadingHyphens_singleHyphen() {
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
    }

    @Test
    public void testStripLeadingHyphens_noHyphen() {
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_leadingQuote() {
        assertEquals("abc\"", Util.stripLeadingAndTrailingQuotes("\"abc\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_trailingQuote() {
        assertEquals("\"abc", Util.stripLeadingAndTrailingQuotes("\"abc\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_bothQuotes() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"abc\""));
    }
}