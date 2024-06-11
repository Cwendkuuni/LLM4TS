package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Util;
import org.junit.Test;

/**
 * tests for Util class
 */
public class UtilTest
{

 @Test
 public void testStripLeadingHyphens()
 {
 String input = "--test--";
 String expectedOutput = "test--";
 String output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "--";
 expectedOutput = "";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "test";
 expectedOutput = "test";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes()
 {
 String input = "\"--test--\"";
 String expectedOutput = "--test--";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "\"--\"";
 expectedOutput = "";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "test";
 expectedOutput = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "\"test\"";
 expectedOutput = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNullInput()
 {
 String input = null;
 String expectedOutput = null;
 String output = Util.stripLeadingHyphens(input);
 assertNull(output);

 input = null;
 expectedOutput = null;
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertNull(output);
 }
}