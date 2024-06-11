import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--str";
        String str2 = "-str";
        String str3 = "str";
        
        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);
        
        Assert.assertEquals("str", result1);
        Assert.assertEquals("str", result2);
        Assert.assertEquals("str", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"one two\"";
        String str2 = "\"one";
        String str3 = "two\"";
        String str4 = "one two";
        
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        
        Assert.assertEquals("one two", result1);
        Assert.assertEquals("one", result2);
        Assert.assertEquals("two", result3);
        Assert.assertEquals("one two", result4);
    }
}