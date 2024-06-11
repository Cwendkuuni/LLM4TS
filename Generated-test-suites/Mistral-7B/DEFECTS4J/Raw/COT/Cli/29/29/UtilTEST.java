import org.junit.Test;
import static org.junit.Assert.*;

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

        input = "";
        expected = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);

        input = "--";
        expected = "";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput() {
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

        input = "\"test\"-\"test\"";
        expected = "test-test";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);

        input = "";
        expected = "";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyInput() {
        String input = "\"\"";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}