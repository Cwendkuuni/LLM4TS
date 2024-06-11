import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NoHyphen_ReturnsSameString() {
        String str = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(str, result);
    }
    
    @Test
    public void testStripLeadingHyphens_SingleHyphen_ReturnsSubstring() {
        String str = "-test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingHyphens_DoubleHyphen_ReturnsSubstring() {
        String str = "--test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes_ReturnsSameString() {
        String str = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(str, result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuote_ReturnsSubstring() {
        String str = "\"test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuote_ReturnsSubstring() {
        String str = "test\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_BothQuotes_ReturnsSubstring() {
        String str = "\"test\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }
}