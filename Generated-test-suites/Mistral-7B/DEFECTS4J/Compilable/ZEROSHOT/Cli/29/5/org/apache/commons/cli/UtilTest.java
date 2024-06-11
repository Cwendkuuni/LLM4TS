package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "--";
 expected = "";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = null;
 expected = null;
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "\"test\"";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }
}