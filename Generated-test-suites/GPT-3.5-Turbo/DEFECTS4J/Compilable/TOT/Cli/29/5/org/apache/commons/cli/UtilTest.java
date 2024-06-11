package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_startsWithDoubleHyphens() {
        assertEquals("string", Util.stripLeadingHyphens("--string"));
    }

    @Test
    public void testStripLeadingHyphens_startsWithSingleHyphen() {
        assertEquals("string", Util.stripLeadingHyphens("-string"));
    }

    @Test
    public void testStripLeadingHyphens_noHyphens() {
        assertEquals("string", Util.stripLeadingHyphens("string"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuoteEndsWithQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuoteDoesNotEndWithQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_doesNotStartWithQuoteEndsWithQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_doesNotStartWithQuoteDoesNotEndWithQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}