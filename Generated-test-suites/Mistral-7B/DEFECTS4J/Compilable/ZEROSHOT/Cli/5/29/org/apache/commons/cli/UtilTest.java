package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input1 = "--option1";
 String expected1 = "option1";
 String result1 = Util.stripLeadingHyphens(input1);
 assertEquals(expected1, result1);

 String input2 = "-option2";
 String expected2 = "option2";
 String result2 = Util.stripLeadingHyphens(input2);
 assertEquals(expected2, result2);

 String input3 = "option3";
 String expected3 = "option3";
 String result3 = Util.stripLeadingHyphens(input3);
 assertEquals(expected3, result3);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input1 = "\"option1\"";
 String expected1 = "option1";
 String result1 = Util.stripLeadingAndTrailingQuotes(input1);
 assertEquals(expected1, result1);

 String input2 = "\"option2 \tmultiple\tspaces\"";
 String expected2 = "option2 multiple spaces";
 String result2 = Util.stripLeadingAndTrailingQuotes(input2);
 assertEquals(expected2, result2);

 String input3 = "option3";
 String expected3 = "option3";
 String result3 = Util.stripLeadingAndTrailingQuotes(input3);
 assertEquals(expected3, result3);
 }
}