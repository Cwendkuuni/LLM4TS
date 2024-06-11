import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    private Util util;

    @Before
    public void setUp() {
        util = new Util();
    }

    // Test for stripLeadingHyphens method

    @Test
    public void testStripLeadingHyphensWithTwoLeadingHyphens() {
        String result = util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphensWithOneLeadingHyphen() {
        String result = util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphensWithNoLeadingHyphen() {
        String result = util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    // Test for stripLeadingAndTrailingQuotes method

    @Test
    public void testStripLeadingAndTrailingQuotesWithLeadingAndTrailingQuotes() {
        String result = util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyLeadingQuote() {
        String result = util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("\"test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyTrailingQuote() {
        String result = util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test\"", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoLeadingOrTrailingQuotes() {
        String result = util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
}