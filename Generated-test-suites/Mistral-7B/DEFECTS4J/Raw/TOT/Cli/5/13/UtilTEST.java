import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--foo";
 String expected = "foo";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "foo";
 expected = "foo";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 str = "-foo";
 expected = "foo";
 actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingHyphensWithEmptyString() {
 String str = "";
 String expected = "";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testStripLeadingHyphensWithNullString() {
 String str = null;
 assertNull(Util.stripLeadingHyphens(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"foo bar\"";
 String expected = "foo bar";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "foo bar";
 expected = "foo bar";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = "";
 expected = "";
 actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 str = null;
 assertNull(Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String str = "\"\"";
 String expected = "";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);
 }
}