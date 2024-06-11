/**
 * JUnit test cases for class Util
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test when str starts with "--"
        String str1 = "--foo";
        String expected1 = "foo";
        assertEquals(expected1, Util.stripLeadingHyphens(str1));
        
        // Test when str starts with "-"
        String str2 = "-f";
        String expected2 = "f";
        assertEquals(expected2, Util.stripLeadingHyphens(str2));
        
        // Test when str does not start with "-"
        String str3 = "foo";
        String expected3 = "foo";
        assertEquals(expected3, Util.stripLeadingHyphens(str3));
        
        // Test when str is null
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test when str starts and ends with quotes
        String str1 = "\"foo\"";
        String expected1 = "foo";
        assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));
        
        // Test when str does not start with quotes but ends with quotes
        String str2 = "foo\"";
        String expected2 = "foo\"";
        assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));
        
        // Test when str starts with quotes but does not end with quotes
        String str3 = "\"foo";
        String expected3 = "\"foo";
        assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(str3));
        
        // Test when str does not start and end with quotes
        String str4 = "foo";
        String expected4 = "foo";
        assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(str4));
        
        // Test when str is an empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}