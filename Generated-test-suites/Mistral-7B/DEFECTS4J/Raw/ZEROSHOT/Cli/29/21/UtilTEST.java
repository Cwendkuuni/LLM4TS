import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test";
 expected = "test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "--";
 expected = "";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = null;
 expected = null;
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "test";
 expected = "test";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"test\"";
 expected = "test";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "";
 expected = "";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullInput() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}