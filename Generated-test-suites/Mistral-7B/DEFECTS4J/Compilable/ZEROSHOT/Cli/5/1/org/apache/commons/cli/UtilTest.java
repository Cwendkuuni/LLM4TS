package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--foo";
 String expectedOutput = "foo";
 String output = Util.stripLeadingHyphens(input);
 Assert.assertEquals(expectedOutput, output);

 input = "--";
 output = Util.stripLeadingHyphens(input);
 Assert.assertEquals("", output);

 input = "foo";
 output = Util.stripLeadingHyphens(input);
 Assert.assertEquals("foo", output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "'one two'";
 String expectedOutput = "one two";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expectedOutput, output);

 input = "\"one two\"";
 output = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals(expectedOutput, output);

 input = "one two";
 output = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals("one two", output);
 }

 @Test
 public void testStripLeadingHyphensWithEmptyString() {
 String input = "";
 String output = Util.stripLeadingHyphens(input);
 Assert.assertEquals("", output);
 }

 @Test
 public void testStripLeadingHyphensWithNullInput() {
 String output = Util.stripLeadingHyphens(null);
 Assert.assertNull(output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String input = "";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 Assert.assertEquals("", output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String output = Util.stripLeadingAndTrailingQuotes(null);
 Assert.assertNull(output);
 }
}