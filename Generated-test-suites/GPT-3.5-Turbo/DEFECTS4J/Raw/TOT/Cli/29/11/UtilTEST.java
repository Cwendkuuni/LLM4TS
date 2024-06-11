import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("str", Util.stripLeadingHyphens("-str"));
        // Edge case
        assertEquals("", Util.stripLeadingHyphens("-"));
        // Error scenario
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Edge case
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        // Error scenario
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}