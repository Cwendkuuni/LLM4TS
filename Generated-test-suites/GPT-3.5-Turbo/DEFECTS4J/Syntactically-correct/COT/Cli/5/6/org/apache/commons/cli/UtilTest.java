
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test with a string starting with "--"
        String str1 = "--test";
        String expected1 = "test";
        Assert.assertEquals(expected1, Util.stripLeadingHyphens(str1));
        
        // Test with a string starting with "-"
        String str2 = "-test";
        String expected2 = "test";
        Assert.assertEquals(expected2, Util.stripLeadingHyphens(str2));
        
        // Test with a string that does not start with hyphen
        String str3 = "test";
        String expected3 = "test";
        Assert.assertEquals(expected3, Util.stripLeadingHyphens(str3));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with a string surrounded by double quotes
        String str1 = "\"test\"";
        String expected1 = "test";
        Assert.assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));
        
        // Test with a string starting with a double quote
        String str2 = "\"test";
        String expected2 = "\"test";
        Assert.assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));
        
        // Test with a string ending with a double quote
        String str3 = "test\"";
        String expected3 = "test\"";
        Assert.assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(str3));
        
        // Test with a string that does not contain double quotes
        String str4 = "test";
        String expected4 = "test";
        Assert.assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(str4));
    }
}
