import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str1 = "--test";
 String str2 = "---Util";
 String str3 = "test";
 String str4 = "Util";
 String expected1 = "test";
 String expected2 = "Util";
 String expected3 = null;
 String actual1 = Util.stripLeadingHyphens(str1);
 String actual2 = Util.stripLeadingHyphens(str2);
 String actual3 = Util.stripLeadingHyphens(str3);
 String actual4 = Util.stripLeadingHyphens(str4);

 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 assertEquals(expected3, actual3);
 assertEquals(expected4, actual4);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str1 = "\"test\"";
 String str2 = "\"Util\"";
 String str3 = "test";
 String str4 = "Util";
 String expected1 = "test";
 String expected2 = "Util";
 String actual1 = Util.stripLeadingAndTrailingQuotes(str1);
 String actual2 = Util.stripLeadingAndTrailingQuotes(str2);
 String actual3 = Util.stripLeadingAndTrailingQuotes(str3);
 String actual4 = Util.stripLeadingAndTrailingQuotes(str4);

 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 assertEquals(actual3, actual3);
 assertEquals(actual4, actual4);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingHyphensWithNullInput() {
 String str = null;
 String actual = Util.stripLeadingHyphens(str);
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWithNullInput() {
 String str = null;
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 }
}