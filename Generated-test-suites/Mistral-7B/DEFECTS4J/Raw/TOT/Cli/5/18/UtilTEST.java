import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "-test";
 expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test";
 expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "test";
 expected = "test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test\" ABC";
 expected = "test ABC";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String str = "";
 Util.stripLeadingAndTrailingQuotes(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithEmptyString() {
 String str = "";
 Util.stripLeadingHyphens(str);
 }
}