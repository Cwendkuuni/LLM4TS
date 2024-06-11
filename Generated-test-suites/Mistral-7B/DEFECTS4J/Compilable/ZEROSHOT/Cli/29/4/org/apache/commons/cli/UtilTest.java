package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(str, result);

 str = null;
 result = Util.stripLeadingHyphens(str);
 assertNull(result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test1 test2\"";
 String expected = "test1 test2";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "test1 test2";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(str, result);

 str = null;
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertNull(result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithOneQuote() {
 String str = "\"test1 test2";
 String expected = "test1 test2";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithLeadingQuoteOnly() {
 String str = "\"test1 test2";
 String expected = "test1 test2";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithTrailingQuoteOnly() {
 String str = "test1 test2\"";
 String expected = "test1 test2";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }
}