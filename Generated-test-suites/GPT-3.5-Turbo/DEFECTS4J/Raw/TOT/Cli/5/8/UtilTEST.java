import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void stripLeadingHyphens_validStartsWithDoubleHyphen_returnStrippedStringWithoutHyphens() {
        String str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void stripLeadingHyphens_validStartsWithSingleHyphen_returnStrippedStringWithoutHyphen() {
        String str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void stripLeadingHyphens_invalidNoHyphen_returnSameString() {
        String str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void stripLeadingAndTrailingQuotes_validStartAndEndsWithQuotes_returnStrippedStringWithoutQuotes() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void stripLeadingAndTrailingQuotes_validStartsWithQuote_returnStrippedStringWithoutLeadingQuote() {
        String str = "\"test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void stripLeadingAndTrailingQuotes_validEndsWithQuote_returnStrippedStringWithoutTrailingQuote() {
        String str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void stripLeadingAndTrailingQuotes_invalidNoQuotes_returnSameString() {
        String str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
}