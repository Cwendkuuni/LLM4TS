import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test null string
        String str = null;
        assertEquals(null, Util.stripLeadingHyphens(str));
        
        // Test string starting with --
        str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));

        // Test string starting with -
        str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));
        
        // Test string without hyphens
        str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test string with leading and trailing quotes
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));

        // Test string with leading quote
        str = "\"test";
        assertEquals("\"test", Util.stripLeadingAndTrailingQuotes(str));

        // Test string with trailing quote
        str = "test\"";
        assertEquals("test\"", Util.stripLeadingAndTrailingQuotes(str));

        // Test string without quotes
        str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

}