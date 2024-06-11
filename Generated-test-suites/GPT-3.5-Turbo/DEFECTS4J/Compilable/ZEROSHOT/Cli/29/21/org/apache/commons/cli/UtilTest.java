package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
  @Test
  public void testStripLeadingHyphens() {
    // Test null input
    assertNull(Util.stripLeadingHyphens(null));
    
    // Test input with "--"
    assertEquals("abc", Util.stripLeadingHyphens("--abc"));
    
    // Test input with "-"
    assertEquals("def", Util.stripLeadingHyphens("-def"));
    
    // Test input without hyphens
    assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Test input with leading and trailing quotes
    assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    
    // Test input with leading quote
    assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"abc"));
    
    // Test input with trailing quote
    assertEquals("def", Util.stripLeadingAndTrailingQuotes("def\""));
    
    // Test input without quotes
    assertEquals("ghi", Util.stripLeadingAndTrailingQuotes("ghi"));
  }
}