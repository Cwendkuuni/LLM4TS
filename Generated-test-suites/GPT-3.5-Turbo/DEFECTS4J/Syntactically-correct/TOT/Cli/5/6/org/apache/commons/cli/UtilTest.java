
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens() {
    // Typical use case: string starts with "--"
    String str1 = "--example";
    assertEquals("example", Util.stripLeadingHyphens(str1));
    
    // Typical use case: string starts with "-"
    String str2 = "-example";
    assertEquals("example", Util.stripLeadingHyphens(str2));
    
    // Edge case: string with no leading hyphens
    String str3 = "example";
    assertEquals("example", Util.stripLeadingHyphens(str3));
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Typical use case: string starts and ends with quotes
    String str1 = "\"example\"";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));
    
   // Typical use case: string starts with quote but doesn't end with quote
    String str2 = "\"example";
    assertEquals("\"example", Util.stripLeadingAndTrailingQuotes(str2));
    
    // Typical use case: string ends with quote but doesn't start with quote
    String str3 = "example\"";
    assertEquals("example\"", Util.stripLeadingAndTrailingQuotes(str3));
    
    // Typical use case: string without quotes
    String str4 = "example";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str4));
    
    // Edge case: empty string
    String str5 = "";
    assertEquals("", Util.stripLeadingAndTrailingQuotes(str5));
  }
}

