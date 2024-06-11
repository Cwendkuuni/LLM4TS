package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    // Test 1: Typical use case - hyphens at the beginning of the string
    String input1 = "--example";
    String expected1 = "example";
    String result1 = Util.stripLeadingHyphens(input1);
    assertEquals(expected1, result1);
    
    // Test 2: Typical use case - a single hyphen at the beginning of the string
    String input2 = "-example";
    String expected2 = "example";
    String result2 = Util.stripLeadingHyphens(input2);
    assertEquals(expected2, result2);
    
    // Test 3: Typical use case - no hyphens at the beginning of the string
    String input3 = "example";
    String expected3 = "example";
    String result3 = Util.stripLeadingHyphens(input3);
    assertEquals(expected3, result3);
    
    // Test 4: Error scenario - null string input
    String input4 = null;
    String result4 = Util.stripLeadingHyphens(input4);
    assertNull(result4);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Test 1: Typical use case - string with leading and trailing quotes
    String input1 = "\"example\"";
    String expected1 = "example";
    String result1 = Util.stripLeadingAndTrailingQuotes(input1);
    assertEquals(expected1, result1);
    
    // Test 2: Typical use case - string with only leading quotes
    String input2 = "\"example";
    String expected2 = "example";
    String result2 = Util.stripLeadingAndTrailingQuotes(input2);
    assertEquals(expected2, result2);
    
    // Test 3: Typical use case - string with only trailing quotes
    String input3 = "example\"";
    String expected3 = "example";
    String result3 = Util.stripLeadingAndTrailingQuotes(input3);
    assertEquals(expected3, result3);
    
    // Test 4: Typical use case - string with no quotes
    String input4 = "example";
    String expected4 = "example";
    String result4 = Util.stripLeadingAndTrailingQuotes(input4);
    assertEquals(expected4, result4);
  }
}