import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical Use Case - String with hyphens at the beginning
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        // Typical Use Case - String with double hyphens at the beginning
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        // Edge Case - String with no hyphens at the beginning
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        // Edge Case - Empty String
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical Use Case - String with leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Edge Case - String with only leading quote
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        // Edge Case - String with only trailing quote
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Edge Case - String with no quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}