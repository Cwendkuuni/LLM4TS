package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    // Typical use case - hyphens at the beginning
    String input1 = "--example";
    String expected1 = "example";
    assertEquals(expected1, Util.stripLeadingHyphens(input1));
    
    // Typical use case - hyphen at the beginning
    String input2 = "-example";
    String expected2 = "example";
    assertEquals(expected2, Util.stripLeadingHyphens(input2));
    
    // Edge case - no hyphens
    String input3 = "example";
    String expected3 = "example";
    assertEquals(expected3, Util.stripLeadingHyphens(input3));
    
    // Error scenario - null input
    String input4 = null;
    assertNull(Util.stripLeadingHyphens(input4));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Typical use case - leading and trailing quotes
    String input1 = "\"example\"";
    String expected1 = "example";
    assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(input1));
    
    // Typical use case - only leading quote
    String input2 = "\"example";
    String expected2 = "\"example";
    assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(input2));
    
    // Typical use case - only trailing quote
    String input3 = "example\"";
    String expected3 = "example\"";
    assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(input3));
    
    // Edge case - no quotes
    String input4 = "example";
    String expected4 = "example";
    assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(input4));
  }
}