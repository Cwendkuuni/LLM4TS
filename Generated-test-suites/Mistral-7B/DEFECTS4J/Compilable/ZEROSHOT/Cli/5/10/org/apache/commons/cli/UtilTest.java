package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "--  test";
 expected = " test";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "\"test \";";
 expected = "test ";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullString() {
 Util.stripLeadingHyphens(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullString() {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}