import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test-string";
 String expected = "test-string";
 String actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "test-string";
 expected = "test-string";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "test-string--";
 expected = "test-string";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test-string\"";
 String expected = "test-string";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "test-string";
 expected = "test-string";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "\"test-string\"";
 expected = "test-string";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "\"test-string\" test-string";
 expected = "test-string test-string";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullString() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullString() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}