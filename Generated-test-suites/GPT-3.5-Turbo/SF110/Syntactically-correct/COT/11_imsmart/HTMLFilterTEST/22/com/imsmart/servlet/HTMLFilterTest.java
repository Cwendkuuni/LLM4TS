import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilterWithEmptyMessage() {
        String message = "";
        String expectedResult = "";
        
        String result = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testFilterWithSpecialCharacters() {
        String message = "<html>Test&";
        String expectedResult = "&lt;html&gt;Test&amp;";
        
        String result = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testFilterWithNullMessage() {
        String message = null;
        String expectedResult = null;
        
        String result = HTMLFilter.filter(message);
        
        Assert.assertEquals(expectedResult, result);
    }
}

