import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingHyphensWithNoHyphen() {
        String result = Util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingHyphensWithNullString() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String result = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyOpeningQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("\"test", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyClosingQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test\"", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String result = Util.stripLeadingAndTrailingQuotes(null);
        assertNull(result);
    }
}