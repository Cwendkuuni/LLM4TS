
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String filteredMessage = HTMLFilter.filter(null);
        Assert.assertNull(filteredMessage);
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String filteredMessage = HTMLFilter.filter("");
        Assert.assertEquals("", filteredMessage);
    }

    @Test
    public void testFilter_MessageWithLessThanSign_ReturnsFilteredString() {
        String message = "This is a <test> message";
        String expectedFilteredMessage = "This is a &lt;test&gt; message";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithGreaterThanSign_ReturnsFilteredString() {
        String message = "This is a >test< message";
        String expectedFilteredMessage = "This is a &gt;test&lt; message";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithAmpersand_ReturnsFilteredString() {
        String message = "This is a &test& message";
        String expectedFilteredMessage = "This is a &amp;test&amp; message";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    @Test
    public void testFilter_MessageWithQuotationMark_ReturnsFilteredString() {
        String message = "This is a \"test\" message";
        String expectedFilteredMessage = "This is a &quot;test&quot; message";
        
        String filteredMessage = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedFilteredMessage, filteredMessage);
    }
    
    // Other test cases for different scenarios can be added here
    
}
