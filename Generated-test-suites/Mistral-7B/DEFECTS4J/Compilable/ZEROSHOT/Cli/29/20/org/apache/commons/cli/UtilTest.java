package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test-string";
 String expected = "test-string";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test-string";
 expected = "test-string";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test-string--";
 expected = "test-string";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = null;
 expected = null;
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test-string\"";
 String expected = "test-string";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "test-string";
 expected = "test-string";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"test-string\"";
 expected = "test-string";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"\"";
 expected = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }
}