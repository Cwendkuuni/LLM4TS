import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }
    
    // Edge case for stripLeadingHyphens() method
    @Test
    public void testStripLeadingHyphensEdgeCase() {
        String input = "-";
        String expected = "";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }
    
    // Edge case for stripLeadingAndTrailingQuotes() method
    @Test
    public void testStripLeadingAndTrailingQuotesEdgeCase() {
        String input = "\"\"";
        String expected = "";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }
    
    // Exception handling scenario for stripLeadingHyphens() method
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingHyphensException() {
        String input = "";
        Util.stripLeadingHyphens(input);
    }
    
    // Exception handling scenario for stripLeadingAndTrailingQuotes() method
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesException() {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}