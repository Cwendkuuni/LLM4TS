import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Basic test case
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));

        // Edge cases
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Basic test case
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        
        // Edge cases
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("\"a\""));
    }

}