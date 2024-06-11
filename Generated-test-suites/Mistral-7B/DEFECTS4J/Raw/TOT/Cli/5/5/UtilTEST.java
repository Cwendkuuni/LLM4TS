import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test-string";
 String expected = "test-string";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test-string";
 expected = "test-string";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test-string--";
 expected = "test-string";
 assertEquals(expected, Util.stripLeadingHyphens(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test-string\"";
 String expected = "test-string";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "test-string";
 expected = "test-string";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test-string\"";
 expected = "test-string";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test-string\"test-string\"";
 expected = "test-stringtest-string";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringForStripLeadingHyphens() {
 String str = null;
 Util.stripLeadingHyphens(str);
 fail("Expected IllegalArgumentException but didn't receive one.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringForStripLeadingAndTrailingQuotes() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 fail("Expected IllegalArgumentException but didn't receive one.");
 }
}