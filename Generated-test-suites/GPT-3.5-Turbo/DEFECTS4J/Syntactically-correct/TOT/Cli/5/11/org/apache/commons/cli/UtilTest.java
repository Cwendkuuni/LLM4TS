import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        String str1 = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str1));
        
        String str2 = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str2));
        
        // Edge cases
        String str3 = "-";
        assertEquals("", Util.stripLeadingHyphens(str3));
        
        String str4 = "--";
        assertEquals("", Util.stripLeadingHyphens(str4));
        
        // Error scenarios
        String str5 = "example";
        assertEquals("example", Util.stripLeadingHyphens(str5));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        String str1 = "\"one two\"";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str1));
        
        String str2 = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str2));
        
        // Edge cases
        String str3 = "\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str3));
        
        String str4 = "\"\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str4));
        
        // Error scenarios
        String str5 = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str5));
    }
}