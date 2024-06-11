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

 String str1 = "foo";
 String actual1 = Util.stripLeadingHyphens(str1);
 assertEquals(str1, actual1);

 String str2 = "-foo";
 String expected2 = "foo";
 String actual2 = Util.stripLeadingHyphens(str2);
 assertEquals(expected2, actual2);
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 String str1 = "one two";
 String actual1 = Util.stripLeadingAndTrailingQuotes(str1);
 assertEquals(str1, actual1);

 String str2 = "\"one two\"";
 String expected2 = "one two";
 String actual2 = Util.stripLeadingAndTrailingQuotes(str2);
 assertEquals(expected2, actual2);

 String str3 = "";
 String actual3 = Util.stripLeadingAndTrailingQuotes(str3);
 assertEquals(str3, actual3);
 }

 // Test for edge case: empty string with leading and trailing quotes
 @Test
 public void testStripLeadingAndTrailingQuotesEdgeCase() {
 String str = "\"\"";
 String expected = "";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }

 // Test for error case: null input
 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesErrorCase() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}