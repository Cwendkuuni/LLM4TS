package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input1 = "--foo";
 String expected1 = "foo";
 String result1 = Util.stripLeadingHyphens(input1);
 assertEquals(expected1, result1);

 String input2 = "-foo";
 String expected2 = "foo";
 String result2 = Util.stripLeadingHyphens(input2);
 assertEquals(expected2, result2);

 String input3 = "foo";
 String expected3 = "foo";
 String result3 = Util.stripLeadingHyphens(input3);
 assertEquals(expected3, result3);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input1 = "'one two'";
 String expected1 = "one two";
 String result1 = Util.stripLeadingAndTrailingQuotes(input1);
 assertEquals(expected1, result1);

 String input2 = "\"one two\"";
 String expected2 = "one two";
 String result2 = Util.stripLeadingAndTrailingQuotes(input2);
 assertEquals(expected2, result2);

 String input3 = "one two";
 String expected3 = "one two";
 String result3 = Util.stripLeadingAndTrailingQuotes(input3);
 assertEquals(expected3, result3);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesEmptyString() {
 String input = "\"\"";
 String expected = "";
 String result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);
 }
}