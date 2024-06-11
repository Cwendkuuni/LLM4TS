import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        
        // Edge cases
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("a-bc", Util.stripLeadingHyphens("a-bc"));
        
        // Error scenarios
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        
        // Edge cases
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one\" two\""));
        assertEquals("\"one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        
        // Error scenarios
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

}