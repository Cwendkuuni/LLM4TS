package org.apache.commons.cli.test;

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

 input = "\"one two\"";
 expectedOutput = "one two";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));

 input = "\"\"";
 expectedOutput = "";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
 }

 // Test for edge cases with leading and trailing hyphens
 @Test
 public void testEdgeCasesLeadingTrailingHyphens() {
 String input = "----test-string----";
 String expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
 String output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, Util.stripLeadingHyphens(output));

 input = "---test-string---";
 expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
 String output2 = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, Util.stripLeadingHyphens(output2));
 }

 // Test for edge cases with leading and trailing quotes
 @Test
 public void testEdgeCasesLeadingTrailingQuotes() {
 String input = "\"--test-string--\"";
 String expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(output));

 input = "--\"test-string\"--";
 expectedOutput = "test-string";
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
 String output3 = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(output3));
 }

 // Test for error cases with invalid input
 @Test (expected = IllegalArgumentException.class)
 public void testErrorCasesInvalidInput() {
 String input = null;
 Util.stripLeadingHyphens(input);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testErrorCasesInvalidInputQuotes() {
 String input = "";
 Util.stripLeadingAndTrailingQuotes(input);
 }
}