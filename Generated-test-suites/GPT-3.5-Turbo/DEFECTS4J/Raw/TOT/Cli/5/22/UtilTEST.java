import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    // Test stripLeadingHyphens method

    @Test
    public void testStripLeadingHyphens_WithDoubleHyphens_ReturnsStrWithoutHyphens() {
        String input = "--input";
        String expected = "input";
        
        String result = Util.stripLeadingHyphens(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStripLeadingHyphens_WithSingleHyphen_ReturnsStrWithoutHyphens() {
        String input = "-input";
        String expected = "input";
        
        String result = Util.stripLeadingHyphens(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStripLeadingHyphens_WithoutHyphen_ReturnsSameStr() {
        String input = "input";
        
        String result = Util.stripLeadingHyphens(input);
        
        assertEquals(input, result);
    }
    
    // Test stripLeadingAndTrailingQuotes method
    
    @Test
    public void testStripLeadingAndTrailingQuotes_WithLeadingAndTrailingQuotes_ReturnsStrWithoutQuotes() {
        String input = "\"input\"";
        String expected = "input";
        
        String result = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_WithLeadingQuotesOnly_ReturnsStrWithoutQuotes() {
        String input = "\"input";
        String expected = "input";
        
        String result = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_WithTrailingQuotesOnly_ReturnsStrWithoutQuotes() {
        String input = "input\"";
        String expected = "input";
        
        String result = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_WithoutQuotes_ReturnsSameStr() {
        String input = "input";
        
        String result = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(input, result);
    }
}