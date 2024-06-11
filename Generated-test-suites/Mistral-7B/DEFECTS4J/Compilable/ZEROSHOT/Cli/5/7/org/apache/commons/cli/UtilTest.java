package org.apache.commons.cli;
import org.apache.commons.cli.Util;
 import org.junit.Test;
 import static org.junit.Assert.*;

 public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String inputWithTwoLeadingHyphens = "--optionA";
 String expectedOutput = "optionA";
 String output = Util.stripLeadingHyphens(inputWithTwoLeadingHyphens);
 assertEquals(expectedOutput, output);

 String inputWithOneLeadingHyphen = "-optionB";
 expectedOutput = "optionB";
 output = Util.stripLeadingHyphens(inputWithOneLeadingHyphen);
 assertEquals(expectedOutput, output);

 String inputWithNoLeadingHyphens = "optionC";
 expectedOutput = "optionC";
 output = Util.stripLeadingHyphens(inputWithNoLeadingHyphens);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String inputWithLeadingQuotes = "'optionD'";
 String expectedOutput = "optionD";
 String output = Util.stripLeadingAndTrailingQuotes(inputWithLeadingQuotes);
 assertEquals(expectedOutput, output);

 String inputWithTrailingQuotes = "optionE''";
 expectedOutput = "optionE";
 output = Util.stripLeadingAndTrailingQuotes(inputWithTrailingQuotes);
 assertEquals(expectedOutput, output);

 String inputWithNeitherLeadingNorTrailingQuotes = "optionF";
 expectedOutput = "optionF";
 output = Util.stripLeadingAndTrailingQuotes(inputWithNeitherLeadingNorTrailingQuotes);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithSingleQuoteAtBeginning() {
 String input = "'";
 String expectedOutput = "";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithSingleQuoteAtEnd() {
 String input = "optionG'";
 String expectedOutput = "optionG";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithDoubleQuotesAtBeginningAndEnd() {
 String input = "\"optionH\"";
 String expectedOutput = "optionH";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }
 }