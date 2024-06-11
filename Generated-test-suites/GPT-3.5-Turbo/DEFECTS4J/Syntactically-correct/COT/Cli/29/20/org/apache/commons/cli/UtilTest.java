import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphensWithHyphenAtStart() {
        String input = "-test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphensWithNoHyphen() {
        String input = "test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphensWithNullInput() {
        String input = null;
        assertNull(Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String input = "test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyLeadingQuotes() {
        String input = "\"test";
        String expected = "\"test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyTrailingQuotes() {
        String input = "test\"";
        String expected = "test\"";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        String input = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(input));
    }

}