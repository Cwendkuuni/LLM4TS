import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String testString = "--test-string";
 String expectedResult = "test-string";
 String result = Util.stripLeadingHyphens(testString);
 assertEquals(expectedResult, result);

 testString = "test-string";
 expectedResult = "test-string";
 result = Util.stripLeadingHyphens(testString);
 assertEquals(expectedResult, result);

 testString = "test-string--";
 expectedResult = "test-string";
 result = Util.stripLeadingHyphens(testString);
 assertEquals(expectedResult, result);

 testString = "";
 expectedResult = "";
 result = Util.stripLeadingHyphens(testString);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String testString = "\"test-string\"";
 String expectedResult = "test-string";
 String result = Util.stripLeadingAndTrailingQuotes(testString);
 assertEquals(expectedResult, result);

 testString = "test-string";
 expectedResult = "test-string";
 result = Util.stripLeadingAndTrailingQuotes(testString);
 assertEquals(expectedResult, result);

 testString = "\"test-string\"";
 expectedResult = "test-string";
 result = Util.stripLeadingAndTrailingQuotes(testString);
 assertEquals(expectedResult, result);

 testString = "";
 expectedResult = "";
 result = Util.stripLeadingAndTrailingQuotes(testString);
 assertEquals(expectedResult, result);
 }
}