import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_NullString_ReturnsNull() {
        String str = null;
        assertNull(Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_HyphenAtBeginning_ReturnsStringWithoutHyphen() {
        String str = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_SingleHyphenAtBeginning_ReturnsStringWithoutHyphen() {
        String str = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_NoHyphen_ReturnsSameString() {
        String str = "example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesAtBothEnds_RemovesQuotes() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes_ReturnsSameString() {
        String str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesAtBeginningOnly_RemovesQuotes() {
        String str = "\"test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesAtEndOnly_RemovesQuotes() {
        String str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesInsideString_ReturnsSameString() {
        String str = "te\"st";
        assertEquals("te\"st", Util.stripLeadingAndTrailingQuotes(str));
    }
}