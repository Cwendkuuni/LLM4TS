import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // typical use case - string starts with two hyphens
        String str1 = "--input";
        assertEquals("input", Util.stripLeadingHyphens(str1));

        // typical use case - string starts with one hyphen
        String str2 = "-output";
        assertEquals("output", Util.stripLeadingHyphens(str2));

        // edge case - string does not start with a hyphen
        String str3 = "number";
        assertEquals("number", Util.stripLeadingHyphens(str3));

        // edge case - string is empty
        String str4 = "";
        assertEquals("", Util.stripLeadingHyphens(str4));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case - string with leading and trailing quotes
        String str1 = "\"Hello World\"";
        assertEquals("Hello World", Util.stripLeadingAndTrailingQuotes(str1));

        // typical use case - string with leading quote only
        String str2 = "\"Apples";
        assertEquals("\"Apples", Util.stripLeadingAndTrailingQuotes(str2));

        // typical use case - string with trailing quote only
        String str3 = "Oranges\"";
        assertEquals("Oranges\"", Util.stripLeadingAndTrailingQuotes(str3));

        // typical use case - string without leading and trailing quotes
        String str4 = "Bananas";
        assertEquals("Bananas", Util.stripLeadingAndTrailingQuotes(str4));

        // edge case - string starts and ends with different quotes
        String str5 = "'Pears\"";
        assertEquals("'Pears\"", Util.stripLeadingAndTrailingQuotes(str5));

        // edge case - string is empty
        String str6 = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str6));

        // error scenario - string is null
        String str7 = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(str7));
    }
}