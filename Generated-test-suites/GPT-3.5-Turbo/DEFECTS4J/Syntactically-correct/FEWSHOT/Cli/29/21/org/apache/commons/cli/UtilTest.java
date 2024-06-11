import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str is null
        assertNull(Util.stripLeadingHyphens(null));

        // Test case 2: str starts with "--"
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));

        // Test case 3: str starts with "-"
        assertEquals("f", Util.stripLeadingHyphens("-f"));

        // Test case 4: str does not start with "--" or "-"
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // Test case 2: str starts with quote and ends with no quote
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));

        // Test case 3: str does not start with quote but ends with quote
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));

        // Test case 4: str does not start or end with quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}