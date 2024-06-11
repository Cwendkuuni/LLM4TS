import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "--";
 result = Util.stripLeadingHyphens(str);
 assertEquals("", result);

 str = "test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(str, result);
 }

 @Test
 public void testStripLeadingHyphensWithNullInput() {
 String str = null;
 String result = Util.stripLeadingHyphens(str);
 assertNull(result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"test\"\t";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "test";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(str, result);

 str = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(str, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String str = null;
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertNull(result);
 }
}