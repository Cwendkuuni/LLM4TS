package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

 str = "---test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = null;
 assertNull(Util.stripLeadingHyphens(str));
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

 str = "\"test\"\n\"test\"";
 expected = "testtest";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = null;
 assertNull(Util.stripLeadingAndTrailingQuotes(str));
 }
}