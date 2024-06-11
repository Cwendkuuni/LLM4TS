package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str is null
        String str1 = null;
        String result1 = Util.stripLeadingHyphens(str1);
        Assert.assertNull(result1);
        
        // Test case 2: str starts with "--"
        String str2 = "--test";
        String result2 = Util.stripLeadingHyphens(str2);
        Assert.assertEquals("test", result2);
        
        // Test case 3: str starts with "-"
        String str3 = "-test";
        String result3 = Util.stripLeadingHyphens(str3);
        Assert.assertEquals("test", result3);
        
        // Test case 4: str does not start with hyphen
        String str4 = "test";
        String result4 = Util.stripLeadingHyphens(str4);
        Assert.assertEquals("test", result4);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts with quote and ends with quote
        String str1 = "\"test\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        Assert.assertEquals("test", result1);
        
        // Test case 2: str starts with quote but does not end with quote
        String str2 = "\"test";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        Assert.assertEquals("\"test", result2);
        
        // Test case 3: str does not start with quote but ends with quote
        String str3 = "test\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        Assert.assertEquals("test\"", result3);
        
        // Test case 4: str does not start with quote and does not end with quote
        String str4 = "test";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        Assert.assertEquals("test", result4);
    }
}