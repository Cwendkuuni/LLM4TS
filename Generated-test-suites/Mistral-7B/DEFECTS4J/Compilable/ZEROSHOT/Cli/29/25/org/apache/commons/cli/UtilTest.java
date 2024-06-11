package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--str";
 String expected = "str";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "str";
 expected = "str";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "str--";
 expected = "str";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "one two";
 expected = "one two";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }

 @Test (expected = NullPointerException.class)
 public void testStripLeadingHyphensWithNullString() {
 Util.stripLeadingHyphens(null);
 }

 @Test (expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWithNullString() {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}