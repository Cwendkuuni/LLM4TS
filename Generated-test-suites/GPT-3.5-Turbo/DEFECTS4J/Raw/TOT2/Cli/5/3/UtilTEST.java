import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("xyz", Util.stripLeadingHyphens("-xyz"));
        assertEquals("123", Util.stripLeadingHyphens("123"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("hello", Util.stripLeadingAndTrailingQuotes("\"hello"));
        assertEquals("world", Util.stripLeadingAndTrailingQuotes("world\""));
    }

    // Edge cases and exception handling scenarios should be tested here

}