import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
        assertEquals("a\"bc", Util.stripLeadingAndTrailingQuotes("a\"bc"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }
}