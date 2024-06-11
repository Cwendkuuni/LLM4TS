import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void filter_NullInput_ReturnsNull() {
        String input = null;
        Assert.assertNull(HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_EmptyString_ReturnsEmptyString() {
        String input = "";
        Assert.assertEquals("", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_NoSpecialCharacters_ReturnsInputString() {
        String input = "This is a test";
        Assert.assertEquals(input, HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_SingleLessThanCharacter_ReplacesWithEntity() {
        String input = "<";
        Assert.assertEquals("&lt;", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_SingleGreaterThanCharacter_ReplacesWithEntity() {
        String input = ">";
        Assert.assertEquals("&gt;", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_SingleAmpersandCharacter_ReplacesWithEntity() {
        String input = "&";
        Assert.assertEquals("&amp;", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_SingleQuoteCharacter_ReturnsUnchanged() {
        String input = "'";
        Assert.assertEquals("'", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_SingleDoubleQuoteCharacter_ReplacesWithEntity() {
        String input = "\"";
        Assert.assertEquals("&quot;", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_CombinationOfSpecialCharacters_ReturnsFilteredString() {
        String input = "This is a <test> & \"sample\"";
        Assert.assertEquals("This is a &lt;test&gt; &amp; &quot;sample&quot;", HTMLFilter.filter(input));
    }
    
    @Test
    public void filter_LargeInput_ReturnsFilteredString() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^*()_+=-{}][|?><.,~`";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^*()_+=-{}][|?><.,~`";
        Assert.assertEquals(expected, HTMLFilter.filter(input));
    }
}