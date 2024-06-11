import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with null input
        assertNull(Util.stripLeadingHyphens(null));
        
        // Test with empty string
        assertEquals("", Util.stripLeadingHyphens(""));

        // Test with leading hyphens
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));

        // Test with multiple leading hyphens
        assertEquals("foo", Util.stripLeadingHyphens("---foo"));

        // Test with no leading hyphens
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Test with no quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));

        // Test with leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // Test with leading and trailing quotes and internal quotes
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"\"\"one two\""));

        // Test with leading quote and no trailing quote
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));

        // Test with no leading quote and trailing quote
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"\"one two\""));
    }
}