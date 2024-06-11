import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc-", Util.stripLeadingHyphens("--abc-"));
        assertEquals("abc--def", Util.stripLeadingHyphens("--abc--def"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\"\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }
}