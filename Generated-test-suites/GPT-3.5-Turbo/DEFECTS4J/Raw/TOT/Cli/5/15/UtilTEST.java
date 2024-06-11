import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical Use Case: String starts with two hyphens
        String str1 = "--test";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("test", result1);

        // Typical Use Case: String starts with one hyphen
        String str2 = "-test";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("test", result2);

        // Edge Case: Empty String
        String str3 = "";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("", result3);

        // Error Scenario: String without hyphens
        String str4 = "test";
        String result4 = Util.stripLeadingHyphens(str4);
        assertEquals("test", result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical Use Case: String with leading and trailing quotes
        String str1 = "\"test\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("test", result1);

        // Edge Case: Empty String
        String str2 = "";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("", result2);

        // Typical Use Case: String without quotes
        String str3 = "test";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("test", result3);

        // Error Scenario: String with only leading quotes
        String str4 = "\"test";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("\"test", result4);

        // Error Scenario: String with only trailing quotes
        String str5 = "test\"";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assertEquals("test\"", result5);
    }

}