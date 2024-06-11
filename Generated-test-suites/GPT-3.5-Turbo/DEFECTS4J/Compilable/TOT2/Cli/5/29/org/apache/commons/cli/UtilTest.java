package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("test", Util.stripLeadingHyphens("--test"));
        assertEquals("test", Util.stripLeadingHyphens("-test"));
        assertEquals("test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("one\"two", Util.stripLeadingAndTrailingQuotes("\"one\"\"two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_edgeCases() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

}