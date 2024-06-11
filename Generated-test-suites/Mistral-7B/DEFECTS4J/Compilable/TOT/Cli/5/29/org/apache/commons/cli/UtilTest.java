package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String str = "--test-string";
 String expected = "test-string";
 String actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "--test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "test-string";
 expected = "test-string";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test-string\"";
 String expected = "test-string";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "\"test\"";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "test-string";
 expected = "test-string";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "\"\"";
 expected = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);
 }

 // Test for edge cases
 @Test
 public void testEdgeCases() {
 String str = "--test--string";
 String expected = "test-string";
 String actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "--test--";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 String strQuotes = "\"test\"test\"";
 String expectedQuotes = "testtest";
 String actualQuotes = Util.stripLeadingAndTrailingQuotes(strQuotes);
 Assert.assertEquals(expectedQuotes, actualQuotes);
 }

 // Test for error scenarios
 @Test(expected = NullPointerException.class)
 public void testNullPointerException() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionQuotes() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}