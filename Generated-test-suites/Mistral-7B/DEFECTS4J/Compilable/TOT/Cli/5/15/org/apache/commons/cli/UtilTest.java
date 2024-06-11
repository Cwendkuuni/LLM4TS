package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String inputString = "--foo";
 String expectedOutput = "foo";
 String actualOutput = Util.stripLeadingHyphens(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "foo";
 expectedOutput = "foo";
 actualOutput = Util.stripLeadingHyphens(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "----foo";
 expectedOutput = "foo";
 actualOutput = Util.stripLeadingHyphens(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "";
 expectedOutput = "";
 actualOutput = Util.stripLeadingHyphens(inputString);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String inputString = "\"one two\"";
 String expectedOutput = "one two";
 String actualOutput = Util.stripLeadingAndTrailingQuotes(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "one two";
 expectedOutput = "one two";
 actualOutput = Util.stripLeadingAndTrailingQuotes(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "\"";
 expectedOutput = "";
 actualOutput = Util.stripLeadingAndTrailingQuotes(inputString);
 assertEquals(expectedOutput, actualOutput);

 inputString = "\"one\"";
 expectedOutput = "one";
 actualOutput = Util.stripLeadingAndTrailingQuotes(inputString);
 assertEquals(expectedOutput, actualOutput);
 }
}