import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case: input starts with "--"
        assertEquals("test", Util.stripLeadingHyphens("--test"));
        
        // Edge case: input starts with "-"
        assertEquals("test", Util.stripLeadingHyphens("-test"));
        
        // Edge case: input does not start with hyphen
        assertEquals("test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case: input has leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        
        // Edge case: input has only leading quote
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        
        // Edge case: input has only trailing quote
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("one two\""));
        
        // Edge case: input has no quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}