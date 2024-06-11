import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str starts with "--"
        String str1 = "--example";
        String expected1 = "example";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals(expected1, result1);

        // Test case 2: str starts with "-"
        String str2 = "-example";
        String expected2 = "example";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals(expected2, result2);

        // Test case 3: str does not start with "-" or "--"
        String str3 = "example";
        String expected3 = "example";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals(expected3, result3);

        // Test case 4: str is null
        String str4 = null;
        String expected4 = null;
        String result4 = Util.stripLeadingHyphens(str4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with quotes
        String str1 = "\"example\"";
        String expected1 = "example";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals(expected1, result1);

        // Test case 2: str starts with a quote and ends without a quote
        String str2 = "\"example";
        String expected2 = "\"example";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals(expected2, result2);

        // Test case 3: str starts without a quote and ends with a quote
        String str3 = "example\"";
        String expected3 = "example\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals(expected3, result3);

        // Test case 4: str does not start or end with quotes
        String str4 = "example";
        String expected4 = "example";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals(expected4, result4);
    }
}