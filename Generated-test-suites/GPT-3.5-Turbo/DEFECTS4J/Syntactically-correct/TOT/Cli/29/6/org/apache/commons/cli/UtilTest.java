import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // typical use case - string starts with two hyphens
        String str1 = "--test";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("test", result1);

        // typical use case - string starts with one hyphen
        String str2 = "-test";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("test", result2);

        // edge case - string is empty
        String str3 = "";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("", result3);

        // error scenario - string is null
        String str4 = null;
        String result4 = Util.stripLeadingHyphens(str4);
        assertNull(result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case - string starts and ends with quotes
        String str5 = "\"test\"";
        String result5 = Util.stripLeadingAndTrailingQuotes(str5);
        assertEquals("test", result5);

        // typical use case - string starts with quote
        String str6 = "\"test";
        String result6 = Util.stripLeadingAndTrailingQuotes(str6);
        assertEquals("test", result6);

        // typical use case - string ends with quote
        String str7 = "test\"";
        String result7 = Util.stripLeadingAndTrailingQuotes(str7);
        assertEquals("test", result7);

        // edge case - string is empty
        String str8 = "";
        String result8 = Util.stripLeadingAndTrailingQuotes(str8);
        assertEquals("", result8);
        
        // error scenario - string is null
        String str9 = null;
        String result9 = Util.stripLeadingAndTrailingQuotes(str9);
        assertNull(result9);
    }
}