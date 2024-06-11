import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "test";
        expected = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "---test";
        expected = "test";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = null;
        expected = null;
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInputThrowsException() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "test";
        expected = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "\"test\"";
        expected = "test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "";
        expected = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }
}