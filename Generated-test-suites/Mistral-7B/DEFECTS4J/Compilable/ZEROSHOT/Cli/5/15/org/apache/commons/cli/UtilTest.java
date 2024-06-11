package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--foo";
 String expectedOutput = "foo";
 String output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "-foo";
 expectedOutput = "foo";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "foo";
 expectedOutput = "foo";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "\"one two\"";
 String expectedOutput = "one two";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "one two";
 expectedOutput = "one two";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "\"";
 expectedOutput = "";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }
}