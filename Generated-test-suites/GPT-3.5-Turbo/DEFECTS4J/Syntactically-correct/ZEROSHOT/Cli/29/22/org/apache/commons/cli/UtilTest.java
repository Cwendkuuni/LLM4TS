import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    /**
     * Test the stripLeadingHyphens method when the input string starts with --
     */
    @Test
    public void testStripLeadingHyphensWithDoubleHyphens() {
        String input = "--string";
        String expected = "string";
        String actual = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingHyphens method when the input string starts with -
     */
    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String input = "-string";
        String expected = "string";
        String actual = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingHyphens method when the input string is null
     */
    @Test
    public void testStripLeadingHyphensWithNullString() {
        String expected = null;
        String actual = Util.stripLeadingHyphens(null);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingAndTrailingQuotes method when the input string starts and ends with quotes
     */
    @Test
    public void testStripLeadingAndTrailingQuotesWithQuotes() {
        String input = "\"string\"";
        String expected = "string";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingAndTrailingQuotes method when the input string starts with a quote
     */
    @Test
    public void testStripLeadingAndTrailingQuotesWithLeadingQuote() {
        String input = "\"string";
        String expected = "string";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingAndTrailingQuotes method when the input string ends with a quote
     */
    @Test
    public void testStripLeadingAndTrailingQuotesWithTrailingQuote() {
        String input = "string\"";
        String expected = "string";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test the stripLeadingAndTrailingQuotes method when the input string does not have any quotes
     */
    @Test
    public void testStripLeadingAndTrailingQuotesWithoutQuotes() {
        String input = "string";
        String expected = "string";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, actual);
    }
}