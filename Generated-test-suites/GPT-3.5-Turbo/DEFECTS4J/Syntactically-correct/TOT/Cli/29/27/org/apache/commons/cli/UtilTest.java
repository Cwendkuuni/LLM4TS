
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str1));

        // Edge case: empty string
        String str2 = "";
        assertEquals("", Util.stripLeadingHyphens(str2));

        // Error scenario: null input
        String str3 = null;
        assertEquals(null, Util.stripLeadingHyphens(str3));

        // Edge case: input with no hyphens
        String str4 = "example";
        assertEquals("example", Util.stripLeadingHyphens(str4));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));

        // Edge case: empty string
        String str2 = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str2));

        // Error scenario: null input
        String str3 = null;
        assertEquals(null, Util.stripLeadingAndTrailingQuotes(str3));

        // Edge case: input with no quotes
        String str4 = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str4));

        // Edge case: input with only leading quote
        String str5 = "\"example";
        assertEquals("\"example", Util.stripLeadingAndTrailingQuotes(str5));

        // Edge case: input with only trailing quote
        String str6 = "example\"";
        assertEquals("example\"", Util.stripLeadingAndTrailingQuotes(str6));
    }
}
