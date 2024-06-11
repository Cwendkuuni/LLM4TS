package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--foo";
 String expected = "foo";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);

 str = "-foo";
 expected = "foo";
 result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);

 str = "foo";
 expected = "foo";
 result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);

 str = "one two";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);

 str = "\"\""; // empty string with leading and trailing quotes
 expected = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullString() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullString() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}