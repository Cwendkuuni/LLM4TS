package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_doubleHyphens() {
        assertEquals("string", Util.stripLeadingHyphens("--string"));
    }

    @Test
    public void testStripLeadingHyphens_singleHyphen() {
        assertEquals("string", Util.stripLeadingHyphens("-string"));
    }

    @Test
    public void testStripLeadingHyphens_noHyphen() {
        assertEquals("string", Util.stripLeadingHyphens("string"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuote() {
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithQuote() {
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_emptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}