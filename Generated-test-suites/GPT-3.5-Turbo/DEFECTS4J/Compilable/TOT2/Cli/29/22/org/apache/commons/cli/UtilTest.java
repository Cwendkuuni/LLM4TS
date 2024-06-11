package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("test", Util.stripLeadingHyphens("--test"));
        assertEquals("test", Util.stripLeadingHyphens("-test"));
        assertEquals("test", Util.stripLeadingHyphens("test"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}