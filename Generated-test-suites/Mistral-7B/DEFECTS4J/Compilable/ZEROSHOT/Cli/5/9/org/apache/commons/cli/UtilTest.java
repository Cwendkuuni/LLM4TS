package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--foo";
 String expected = "foo";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "foo";
 expected = "foo";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "--";
 expected = "";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "one two";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"";
 expected = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}