package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

   @Test
   public void testStripLeadingHyphens() {
       // Test with input starting with "--"
       String input1 = "--test";
       String expected1 = "test";
       assertEquals(expected1, Util.stripLeadingHyphens(input1));

       // Test with input starting with "-"
       String input2 = "-test";
       String expected2 = "test";
       assertEquals(expected2, Util.stripLeadingHyphens(input2));

       // Test with input not starting with "-"
       String input3 = "test";
       String expected3 = "test";
       assertEquals(expected3, Util.stripLeadingHyphens(input3));
   }

   @Test
   public void testStripLeadingAndTrailingQuotes() {
       // Test with input starting and ending with quotes
       String input1 = "\"test\"";
       String expected1 = "test";
       assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(input1));

       // Test with input starting with quote but not ending with quote
       String input2 = "\"test";
       String expected2 = "\"test";
       assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(input2));

       // Test with input ending with quote but not starting with quote
       String input3 = "test\"";
       String expected3 = "test\"";
       assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(input3));

       // Test with input not starting and ending with quotes
       String input4 = "test";
       String expected4 = "test";
       assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(input4));
   }
}