import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // Typical case
        String str1 = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str1));
        
        String str2 = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str2));
        
        // Edge cases
        String str3 = "--";
        assertEquals("", Util.stripLeadingHyphens(str3));
        
        String str4 = "-";
        assertEquals("", Util.stripLeadingHyphens(str4));
        
        // Error scenario
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical case
        String str1 = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));
        
        // Edge cases
        String str2 = "\"\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str2));
        
        // Error scenario
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}