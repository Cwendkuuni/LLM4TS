package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String input = "--test-string";
 String expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

 input = "test-string";
 expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));

 input = "--";
 expectedOutput = "";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "'one two'";
 String expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

 input = "one two";
 expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

 input = "\"one two\"";
 expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

 input = "\"one two\"'";
 expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
 }

 // Test for edge cases
 @Test
 public void testStripLeadingHyphensEdgeCase() {
 String input = "--test-string--";
 String expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
 }

 @Test
 public void testStripLeadingAndTrailingQuotesEdgeCase() {
 String input = "\"one two\"\"";
 String expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
 }

 // Test for error scenarios
 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensNullInput() {
 Util.stripLeadingHyphens(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesNullInput() {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}