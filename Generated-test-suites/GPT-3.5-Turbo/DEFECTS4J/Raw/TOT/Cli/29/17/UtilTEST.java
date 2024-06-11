
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical cases
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));

        // Edge cases
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("abc-", Util.stripLeadingHyphens("abc-"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));

        // Edge cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("abc\"", Util.stripLeadingAndTrailingQuotes("abc\""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}

