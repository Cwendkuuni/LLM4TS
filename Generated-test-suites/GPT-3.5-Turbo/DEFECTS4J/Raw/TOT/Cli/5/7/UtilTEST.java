import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));

        // Edge case
        assertEquals("", Util.stripLeadingHyphens("--"));

        // Error scenario
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // Edge case
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Error scenario
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}