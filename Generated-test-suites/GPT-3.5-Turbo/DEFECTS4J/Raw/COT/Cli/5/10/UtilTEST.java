import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";

        assertEquals("test", Util.stripLeadingHyphens(str1));
        assertEquals("test", Util.stripLeadingHyphens(str2));
        assertEquals("test", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "test";
        String str3 = "\"test";

        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str2));
        assertEquals("\"test", Util.stripLeadingAndTrailingQuotes(str3));
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testStripLeadingHyphensWithEmptyString() {
        String str = "";

        assertEquals("", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String str = "";

        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String str = "test";

        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithSingleQuote() {
        String str = "\"";

        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }
}


// The complete JUnit test file should start with 

// All the individual test cases go here

// The complete JUnit test file should end with 
