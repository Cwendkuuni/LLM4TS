package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String str = "--foo";
 String expected = "foo";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "-foo";
 expected = "foo";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "foo";
 expected = "foo";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "\"one\"";
 expected = "one";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "one";
 expected = "one";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }

 // Test for edge cases
 @Test
 public void testEdgeCases() {
 String str = "--foo--";
 String expected = "foo";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 String strQuotes = "\"one two\"\"three four\"";
 String expectedQuotes = "one twothree four";
 String actualQuotes = Util.stripLeadingAndTrailingQuotes(strQuotes);
 assertEquals(expectedQuotes, actualQuotes);
 }

 // Test for error scenarios
 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionQuotes() {
 String str = "";
 Util.stripLeadingAndTrailingQuotes(str);
 }
}