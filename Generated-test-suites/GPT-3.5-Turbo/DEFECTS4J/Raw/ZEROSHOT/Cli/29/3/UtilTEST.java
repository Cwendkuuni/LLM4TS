import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Testing null string
        assertNull(Util.stripLeadingHyphens(null));

        // Testing string with double hyphens
        String str1 = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str1));

        // Testing string with single hyphen
        String str2 = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str2));
        
        // Testing string without hyphens
        String str3 = "example";
        assertEquals("example", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Testing string with leading and trailing quotes
        String str1 = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));

        // Testing string with leading quote only
        String str2 = "\"example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str2));
        
        // Testing string with trailing quote only
        String str3 = "example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str3));
        
        // Testing string without quotes
        String str4 = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str4));
    }
}