import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with string starting with "--"
        String str1 = "--testString";
        String expected1 = "testString";
        assertEquals(expected1, Util.stripLeadingHyphens(str1));
        
        // Test with string starting with "-"
        String str2 = "-testString";
        String expected2 = "testString";
        assertEquals(expected2, Util.stripLeadingHyphens(str2));
        
        // Test with string not starting with hyphen
        String str3 = "testString";
        assertEquals(str3, Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with string starting and ending with quotes
        String str1 = "\"testString\"";
        String expected1 = "testString";
        assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));
        
        // Test with string starting with quotes only
        String str2 = "\"testString";
        String expected2 = "\"testString";
        assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));
        
        // Test with string ending with quotes only
        String str3 = "testString\"";
        String expected3 = "testString\"";
        assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(str3));
        
        // Test with string not starting or ending with quotes
        String str4 = "testString";
        assertEquals(str4, Util.stripLeadingAndTrailingQuotes(str4));
    }
}