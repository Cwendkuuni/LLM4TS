package org.apache.commons.cli;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
  
  private static Util util;
  
  @BeforeClass
  public static void setUp() {
    util = new Util();
  }
  
  @Test
  public void testStripLeadingHyphens() {
    // Test for string starting with "--"
    String input1 = "--test";
    String expected1 = "test";
    String result1 = util.stripLeadingHyphens(input1);
    assertEquals(expected1, result1);
    
    // Test for string starting with "-"
    String input2 = "-test";
    String expected2 = "test";
    String result2 = util.stripLeadingHyphens(input2);
    assertEquals(expected2, result2);
    
    // Test for string not starting with hyphen
    String input3 = "test";
    String expected3 = "test";
    String result3 = util.stripLeadingHyphens(input3);
    assertEquals(expected3, result3);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Test for string with leading and trailing quotes
    String input1 = "\"test\"";
    String expected1 = "test";
    String result1 = util.stripLeadingAndTrailingQuotes(input1);
    assertEquals(expected1, result1);
    
    // Test for string with leading quote and no trailing quote
    String input2 = "\"test";
    String expected2 = "test";
    String result2 = util.stripLeadingAndTrailingQuotes(input2);
    assertEquals(expected2, result2);
    
    // Test for string with trailing quote and no leading quote
    String input3 = "test\"";
    String expected3 = "test";
    String result3 = util.stripLeadingAndTrailingQuotes(input3);
    assertEquals(expected3, result3);
    
    // Test for string without any quotes
    String input4 = "test";
    String expected4 = "test";
    String result4 = util.stripLeadingAndTrailingQuotes(input4);
    assertEquals(expected4, result4);
  }
}