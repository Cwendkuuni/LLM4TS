import junit.framework.TestCase;

public class UtilTest extends TestCase {

    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";
        String expected1 = "test";
        String expected2 = "test";
        String expected3 = "test";
        assertEquals(expected1, Util.stripLeadingHyphens(str1));
        assertEquals(expected2, Util.stripLeadingHyphens(str2));
        assertEquals(expected3, Util.stripLeadingHyphens(str3));
    }

    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "test";
        String expected1 = "test";
        String expected2 = "test";
        assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));
    }
}