import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        input = "---test";
        expectedOutput = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
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
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "\"test1 test2\"\"";
        expectedOutput = "test1 test2";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        input = "test1 test2";
        expectedOutput = "test1 test2";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}