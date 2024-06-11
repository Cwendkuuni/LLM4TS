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
 result = Util.stripLeadingHyphens(str);
 assertEquals(str, result);

 str = "";
 result = Util.stripLeadingHyphens(str);
 assertEquals(str, result);
 }

 @Test
 public void testStripLeadingHyphensWithMultipleHyphens() {
 String str = "--test---";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingHyphensWithEmptyString() {
 String str = "";
 String result = Util.stripLeadingHyphens(str);
 assertEquals(str, result);
 }

 @Test
 public void testStripLeadingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingQuotesWithNoLeadingQuotes() {
 String str = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(str, result);
 }

 @Test
 public void testStripLeadingQuotesWithTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingQuotesWithBothLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 String expected = "test";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullString() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingQuotesWithNullString() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}