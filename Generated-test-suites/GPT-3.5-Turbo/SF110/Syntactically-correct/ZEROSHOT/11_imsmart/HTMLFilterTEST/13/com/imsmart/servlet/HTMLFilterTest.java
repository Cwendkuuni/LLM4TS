
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        String input = "Hello World!";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(input, result);
    }
    
    @Test
    public void testFilter_ContainsLessThan_ReturnsConvertedString() {
        String input = "This is < an example";
        String expected = "This is &lt; an example";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsGreaterThan_ReturnsConvertedString() {
        String input = "This is > an example";
        String expected = "This is &gt; an example";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsAmpersand_ReturnsConvertedString() {
        String input = "This is & an example";
        String expected = "This is &amp; an example";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsDoubleQuotes_ReturnsConvertedString() {
        String input = "This is \" an example";
        String expected = "This is &quot; an example";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_MultipleSpecialCharacters_ReturnsConvertedString() {
        String input = "This is < & > \" an example";
        String expected = "This is &lt; &amp; &gt; &quot; an example";
        String result = HTMLFilter.filter(input);
        Assert.assertEquals(expected, result);
    }
    
    // Add more test methods for other scenarios if needed
    
}
