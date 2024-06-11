
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    // Test case 1: str starts with "--"
    String str1 = "--abc";
    Assert.assertEquals("abc", Util.stripLeadingHyphens(str1));
    
    // Test case 2: str starts with "-"
    String str2 = "-xyz";
    Assert.assertEquals("xyz", Util.stripLeadingHyphens(str2));
    
    // Test case 3: str does not start with "-"
    String str3 = "def";
    Assert.assertEquals("def", Util.stripLeadingHyphens(str3));
    
    // Test case 4: str is null
    String str4 = null;
    Assert.assertNull(Util.stripLeadingHyphens(str4));
    
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Test case 1: str starts with " and ends with "
    String str1 = "\"one two\"";
    Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str1));
    
    // Test case 2: str starts with " and does not end with "
    String str2 = "\"two three";
    Assert.assertEquals("two three", Util.stripLeadingAndTrailingQuotes(str2));
    
    // Test case 3: str does not start with " and ends with "
    String str3 = "one two\"";
    Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str3));
    
    // Test case 4: str does not start with " and does not end with "
    String str4 = "three four";
    Assert.assertEquals("three four", Util.stripLeadingAndTrailingQuotes(str4));
    
  }
}
