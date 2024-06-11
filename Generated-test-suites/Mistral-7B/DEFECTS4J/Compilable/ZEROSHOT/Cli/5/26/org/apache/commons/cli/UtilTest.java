package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String strWithLeadingHyphens = "--option";
 String expected = "option";
 String result = Util.stripLeadingHyphens(strWithLeadingHyphens);
 assertEquals(expected, result);

 String strWithoutLeadingHyphens = "option";
 String emptyResult = Util.stripLeadingHyphens(strWithoutLeadingHyphens);
 assertEquals(strWithoutLeadingHyphens, emptyResult);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String strWithLeadingAndTrailingQuotes = "\"one two\"";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes);
 assertEquals(expected, result);

 String strWithLeadingQuotesOnly = "\"one two\"";
 String resultWithLeadingQuotesOnly = Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotesOnly);
 assertEquals("one two", resultWithLeadingQuotesOnly);

 String strWithTrailingQuotesOnly = "\"one two\"";
 String resultWithTrailingQuotesOnly = Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotesOnly);
 assertEquals("one two", resultWithTrailingQuotesOnly);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesEmptyString() {
 String emptyString = "";
 String result = Util.stripLeadingAndTrailingQuotes(emptyString);
 assertEquals(emptyString, result);
 }
}