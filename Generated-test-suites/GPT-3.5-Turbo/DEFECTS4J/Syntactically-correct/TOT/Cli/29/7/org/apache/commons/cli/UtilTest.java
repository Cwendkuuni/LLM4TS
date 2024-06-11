import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput_ReturnNull() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_StrStartsWithDoubleHyphen_ReturnStrWithoutHyphens() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_StrStartsWithSingleHyphen_ReturnStrWithoutHyphen() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_StrDoesNotStartWithHyphen_ReturnSameStr() {
        String result = Util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StrStartsAndEndsWithQuotes_ReturnStrWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StrStartsWithQuoteAndNotEndsWithQuote_ReturnStrWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StrNotStartsWithQuoteAndEndsWithQuote_ReturnStrWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StrDoesNotStartAndEndWithQuotes_ReturnSameStr() {
        String result = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
}