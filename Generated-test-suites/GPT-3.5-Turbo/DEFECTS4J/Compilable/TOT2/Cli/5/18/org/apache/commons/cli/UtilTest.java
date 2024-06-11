package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    String result1 = Util.stripLeadingHyphens("--test");
    String result2 = Util.stripLeadingHyphens("-test");
    String result3 = Util.stripLeadingHyphens("test");
    
    assertEquals("test", result1);
    assertEquals("test", result2);
    assertEquals("test", result3);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    String result1 = Util.stripLeadingAndTrailingQuotes("\"test\"");
    String result2 = Util.stripLeadingAndTrailingQuotes("test");
    
    assertEquals("test", result1);
    assertEquals("test", result2);
  }
}