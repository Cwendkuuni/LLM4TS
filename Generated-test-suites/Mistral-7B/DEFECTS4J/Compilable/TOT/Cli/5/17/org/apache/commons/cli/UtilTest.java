package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--foo";
 String expected = "foo";
 String result = Util.stripLeadingHyphens(input);
 assertEquals(expected, result);

 input = "-foo";
 expected = "foo";
 result = Util.stripLeadingHyphens(input);
 assertEquals(expected, result);

 input = "foo";
 expected = "foo";
 result = Util.stripLeadingHyphens(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "\"one two\"";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);

 input = "'one two'";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);

 input = "one two";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);

 input = "\"\"";
 expected = "";
 result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullInput() {
 Util.stripLeadingHyphens(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}