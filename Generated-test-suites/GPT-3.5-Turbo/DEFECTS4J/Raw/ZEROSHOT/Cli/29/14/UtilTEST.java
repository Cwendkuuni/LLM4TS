// JUnit test for Util class

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String input = "-test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String input = "test";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }
}