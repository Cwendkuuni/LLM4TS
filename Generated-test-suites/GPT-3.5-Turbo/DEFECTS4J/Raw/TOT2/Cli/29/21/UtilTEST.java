import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}