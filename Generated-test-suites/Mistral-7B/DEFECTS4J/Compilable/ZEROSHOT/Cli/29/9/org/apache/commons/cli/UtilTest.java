package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input1 = "--test";
 String expected1 = "test";
 String actual1 = Util.stripLeadingHyphens(input1);
 assertEquals(expected1, actual1);

 String input2 = "--Test";
 String expected2 = "Test";
 String actual2 = Util.stripLeadingHyphens(input2);
 assertEquals(expected2, actual2);

 String input3 = "test";
 String expected3 = "test";
 String actual3 = Util.stripLeadingHyphens(input3);
 assertEquals(expected3, actual3);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input1 = "\"--test\"";
 String expected1 = "test";
 String actual1 = Util.stripLeadingAndTrailingQuotes(input1);
 assertEquals(expected1, actual1);

 String input2 = "\"--Test\"";
 String expected2 = "Test";
 String actual2 = Util.stripLeadingAndTrailingQuotes(input2);
 assertEquals(expected2, actual2);

 String input3 = "\"test\"";
 String expected3 = "test";
 String actual3 = Util.stripLeadingAndTrailingQuotes(input3);
 assertEquals(expected3, actual3);

 String input4 = "\"Test\"";
 String expected4 = "Test";
 String actual4 = Util.stripLeadingAndTrailingQuotes(input4);
 assertEquals(expected4, actual4);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesEmptyString() {
 String input = "\"\"";
 String expected = "";
 String actual = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingHyphensWithNullInput() {
 String input = null;
 Util.stripLeadingHyphens(input);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String input = null;
 Util.stripLeadingAndTrailingQuotes(input);
 }
}