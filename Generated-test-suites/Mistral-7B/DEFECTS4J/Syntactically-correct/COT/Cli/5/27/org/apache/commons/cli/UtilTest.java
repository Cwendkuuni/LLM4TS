import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--foo";
        String expectedResult = "foo";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expectedResult, result);

        str = "-foo";
        expectedResult = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expectedResult, result);

        str = "foo";
        expectedResult = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expectedResult, result);

        // Test edge case with empty string
        str = "";
        expectedResult = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingHyphensWithNullString() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expectedResult = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);

        str = "one two";
        expectedResult = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);

        str = "";
        expectedResult = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);

        str = "\"";
        expectedResult = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);

        str = "\"one\"";
        expectedResult = "one";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);

        str = "\"one two\""; // Test case with only one trailing quote
        expectedResult = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}