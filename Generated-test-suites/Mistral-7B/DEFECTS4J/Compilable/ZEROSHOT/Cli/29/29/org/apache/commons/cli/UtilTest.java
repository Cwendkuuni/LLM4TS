package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--test";
 String expected = "test";
 String result = Util.stripLeadingHyphens(input);
 assertEquals(expected, result);

 input = "test";
 result = Util.stripLeadingHyphens(input);
 assertEquals(input, result);

 input = null;
 result = Util.stripLeadingHyphens(input);
 assertNull(result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);

 input = "test";
 result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(input, result);

 input = "";
 result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(input, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String input = "\"\"";
 String expected = "";
 String result = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expected, result);
 }
}