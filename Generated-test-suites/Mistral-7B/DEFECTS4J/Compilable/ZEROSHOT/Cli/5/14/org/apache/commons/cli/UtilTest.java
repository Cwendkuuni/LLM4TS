package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String strWithLeadingHyphens = "--test-string";
 String expected = "test-string";
 String result = Util.stripLeadingHyphens(strWithLeadingHyphens);
 assertEquals(expected, result);

 String strWithoutLeadingHyphens = "test-string";
 String strWithSingleLeadingHyphen = "-test-string";
 String strWithoutLeadingHyphen = "test-string";
 String resultWithoutLeadingHyphen = Util.stripLeadingHyphens(strWithSingleLeadingHyphen);
 assertEquals(strWithoutLeadingHyphen, resultWithoutLeadingHyphen);
 assertEquals(strWithoutLeadingHyphens, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String strWithLeadingAndTrailingQuotes = "\"test-string\"";
 String expected = "test-string";
 String result = Util.stripLeadingAndTrailingQuotes(strWithLeadingAndTrailingQuotes);
 assertEquals(expected, result);

 String strWithLeadingQuotes = "\"test-string\"";
 String strWithoutLeadingQuotes = "test-string";
 String resultWithoutLeadingQuotes = Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotes);
 assertEquals(strWithoutLeadingQuotes, resultWithoutLeadingQuotes);
 assertEquals(strWithoutLeadingQuotes, result);

 String strWithTrailingQuotes = "test-string\"";
 String strWithoutTrailingQuotes = "test-string";
 String resultWithoutTrailingQuotes = Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotes);
 assertEquals(strWithoutTrailingQuotes, resultWithoutTrailingQuotes);
 assertEquals(strWithoutTrailingQuotes, result);
 }
}