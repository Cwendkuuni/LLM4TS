package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        assertEquals("test", Util.stripLeadingHyphens("--test"));
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        assertEquals("test", Util.stripLeadingHyphens("-test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyLeadingQuotes() {
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyTrailingQuotes() {
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
    }
}