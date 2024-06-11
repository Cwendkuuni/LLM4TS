import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens(){
 String inputStr = "--test-string";
 String expectedOutput = "test-string";
 String outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingHyphensWhenInputIsEmptyOrNull(){
 String inputStr = null;
 String expectedOutput = null;
 String outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "";
 expectedOutput = "";
 outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingHyphensWhenInputHasOneHyphen(){
 String inputStr = "--test";
 String expectedOutput = "test";
 String outputStr = Util.stripLeadingHyphens(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes(){
 String inputStr = "\"test-string\"";
 String expectedOutput = "test-string";
 String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWhenInputIsEmptyOrNull(){
 String inputStr = null;
 String expectedOutput = null;
 String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);

 inputStr = "";
 expectedOutput = "";
 outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 assertEquals(expectedOutput, outputStr);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWhenInputStartsWithTwoQuotes(){
 String inputStr = "\"\"test-string\"";
 String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
 }
}