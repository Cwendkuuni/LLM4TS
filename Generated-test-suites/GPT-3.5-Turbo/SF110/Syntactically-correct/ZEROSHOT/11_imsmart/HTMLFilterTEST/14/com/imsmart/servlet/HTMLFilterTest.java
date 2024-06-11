import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String message = null;
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertNull(filteredMessage);
    }
    
    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String message = "";
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals("", filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithLessThanSymbol_ReplacesWithHtmlEntity() {
        String message = "This is < a test";
        String expected = "This is &lt; a test";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithGreaterThanSymbol_ReplacesWithHtmlEntity() {
        String message = "This is > a test";
        String expected = "This is &gt; a test";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithAmpersandSymbol_ReplacesWithHtmlEntity() {
        String message = "This is & a test";
        String expected = "This is &amp; a test";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithQuoteSymbol_ReplacesWithHtmlEntity() {
        String message = "This is \" a test";
        String expected = "This is &quot; a test";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expected, filteredMessage);
    }
}

