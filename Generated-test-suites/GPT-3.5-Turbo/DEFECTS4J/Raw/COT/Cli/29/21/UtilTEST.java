import org.junit.Test;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    String result = Util.stripLeadingHyphens("--string");
    assertEquals("string", result);
    
    result = Util.stripLeadingHyphens("-string");
    assertEquals("string", result);
    
    result = Util.stripLeadingHyphens("string");
    assertEquals("string", result);
    
    result = Util.stripLeadingHyphens(null);
    assertNull(result);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    String result = Util.stripLeadingAndTrailingQuotes("\"string\"");
    assertEquals("string", result);
    
    result = Util.stripLeadingAndTrailingQuotes("string");
    assertEquals("string", result);
    
    result = Util.stripLeadingAndTrailingQuotes("\"string");
    assertEquals("\"string", result);
    
    result = Util.stripLeadingAndTrailingQuotes("string\"");
    assertEquals("string\"", result);
  }
  
}