import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test";
 expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--  test";
 expected = " test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--test--";
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

 str = "\"test\"";
 expected = "test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test \"";
 expected = "test ";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test \"test\"";
 expected = "test test";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
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