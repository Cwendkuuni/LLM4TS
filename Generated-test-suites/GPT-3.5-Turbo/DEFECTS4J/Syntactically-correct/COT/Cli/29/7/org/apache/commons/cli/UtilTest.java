
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString_returnNull() {
        assertEquals(null, Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_hyphensAtStartOfString_returnStringWithoutHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
    }

    @Test
    public void testStripLeadingHyphens_noHyphensAtStartOfString_returnSameString() {
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startAndEndWithQuotes_returnStringWithoutQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startWithQuote_returnStringWithoutLeadingQuote() {
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_endWithQuote_returnStringWithoutTrailingQuote() {
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotesAtStartAndEndOfString_returnSameString() {
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
    }
}
