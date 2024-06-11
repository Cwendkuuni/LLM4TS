import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two ", Util.stripLeadingAndTrailingQuotes("\"one two \""));
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"\"one two"));
        assertEquals("\"one two\" and \"three four\"", Util.stripLeadingAndTrailingQuotes("\"\"one two\" and \"three four\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}