package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphensWithNullString() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphensWithDoubleHyphens() {
        String result = Util.stripLeadingHyphens("--str");
        assertEquals("str", result);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String result = Util.stripLeadingHyphens("-str");
        assertEquals("str", result);
    }

    @Test
    public void testStripLeadingHyphensWithNoHyphen() {
        String result = Util.stripLeadingHyphens("str");
        assertEquals("str", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithLeadingQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("\"one two\"");
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithTrailingQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("\"one two");
        assertEquals("\"one two", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithBothQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"one two\"");
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("one two");
        assertEquals("one two", result);
    }
}