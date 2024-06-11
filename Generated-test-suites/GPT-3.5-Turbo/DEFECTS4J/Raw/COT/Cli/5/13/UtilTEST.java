import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with single hyphen at the beginning
        String str1 = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str1));

        // Test with double hyphens at the beginning
        String str2 = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str2));

        // Test with no hyphen at the beginning
        String str3 = "test";
        assertEquals("test", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with leading and trailing double quotes
        String str1 = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));

        // Test with leading double quote
        String str2 = "\"test";
        assertEquals("\"test", Util.stripLeadingAndTrailingQuotes(str2));

        // Test with trailing double quote
        String str3 = "test\"";
        assertEquals("test\"", Util.stripLeadingAndTrailingQuotes(str3));

        // Test with no double quotes
        String str4 = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str4));
    }

}