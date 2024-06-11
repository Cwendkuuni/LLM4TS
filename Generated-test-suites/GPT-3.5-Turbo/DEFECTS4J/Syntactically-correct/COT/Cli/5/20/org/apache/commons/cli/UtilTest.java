
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.cli.Util;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test case: str starts with "--"
        String str1 = "--testString";
        String expectedResult1 = "testString";
        assertEquals(expectedResult1, Util.stripLeadingHyphens(str1));

        // Test case: str starts with "-"
        String str2 = "-testString";
        String expectedResult2 = "testString";
        assertEquals(expectedResult2, Util.stripLeadingHyphens(str2));

        // Test case: str does not start with hyphen
        String str3 = "testString";
        String expectedResult3 = "testString";
        assertEquals(expectedResult3, Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case: str starts and ends with quotes
        String str1 = "\"testString\"";
        String expectedResult1 = "testString";
        assertEquals(expectedResult1, Util.stripLeadingAndTrailingQuotes(str1));

        // Test case: str starts with a quote
        String str2 = "\"testString";
        String expectedResult2 = "testString";
        assertEquals(expectedResult2, Util.stripLeadingAndTrailingQuotes(str2));

        // Test case: str ends with a quote
        String str3 = "testString\"";
        String expectedResult3 = "testString";
        assertEquals(expectedResult3, Util.stripLeadingAndTrailingQuotes(str3));

        // Test case: str does not start or end with quotes
        String str4 = "testString";
        String expectedResult4 = "testString";
        assertEquals(expectedResult4, Util.stripLeadingAndTrailingQuotes(str4));
    }
}
