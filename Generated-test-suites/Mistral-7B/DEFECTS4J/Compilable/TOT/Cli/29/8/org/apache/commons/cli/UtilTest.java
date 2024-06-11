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

 str = "foo--bar";
 expected = "foo bar";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"foo bar\"";
 String expected = "foo bar";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "foo bar";
 expected = "foo bar";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingHyphensWithNullInput() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testStripLeadingHyphensWithEmptyString() {
 String str = "";
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String str = "";
 Util.stripLeadingAndTrailingQuotes(str);
 }
}