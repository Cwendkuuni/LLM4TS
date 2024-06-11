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

 str = "-test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "test";
 expected = "test";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test one two\"";
 String expected = "test one two";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "test one two";
 expected = "test one two";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "\"test\"";
 expected = "test";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }
}