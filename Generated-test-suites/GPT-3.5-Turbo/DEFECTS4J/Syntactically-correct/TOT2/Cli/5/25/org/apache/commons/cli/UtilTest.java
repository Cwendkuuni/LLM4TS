import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test"));
    }

    // Edge cases and error scenarios
    @Test
    public void testStripLeadingHyphensEdgeCase() {
        assertEquals("xy", Util.stripLeadingHyphens("-xy"));
        assertEquals("", Util.stripLeadingHyphens("-"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEdgeCase() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}