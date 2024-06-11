package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        assertEquals("", Util.stripLeadingHyphens("--"));
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        assertEquals("", Util.stripLeadingHyphens("-"));
    }

    @Test
    public void testStripLeadingHyphens_NoHyphens() {
        assertEquals("example", Util.stripLeadingHyphens("example"));
    }

    @Test
    public void testStripLeadingHyphens_HyphenAtStart() {
        assertEquals("example", Util.stripLeadingHyphens("-example"));
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphenAtStart() {
        assertEquals("example", Util.stripLeadingHyphens("--example"));
    }

    @Test
    public void testStripLeadingHyphens_HyphenInMiddle() {
        assertEquals("example-one", Util.stripLeadingHyphens("example-one"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("example"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_SingleQuoteAtStart() {
        assertEquals("example'", Util.stripLeadingAndTrailingQuotes("'example'"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_SingleQuoteAtEnd() {
        assertEquals("'example", Util.stripLeadingAndTrailingQuotes("'example'"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_DoubleQuotes() {
        assertEquals("example", Util.stripLeadingAndTrailingQuotes("\"example\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuotes() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesInsideString() {
        assertEquals("\"example\"", Util.stripLeadingAndTrailingQuotes("\"\"example\"\""));
    }
}