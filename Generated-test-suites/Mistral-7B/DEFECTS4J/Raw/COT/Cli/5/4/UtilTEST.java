import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        input = "test";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        input = "-test";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

        // Edge case: empty string
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test1 test2\"";
        String expectedOutput = "test1 test2";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        input = "test1 test2";
        expectedOutput = "test1 test2";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        // Edge case: string with only leading quotes
        input = "\"test";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

        // Edge case: string with only trailing quotes
        input = "test\"";
        expectedOutput = "test";
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}