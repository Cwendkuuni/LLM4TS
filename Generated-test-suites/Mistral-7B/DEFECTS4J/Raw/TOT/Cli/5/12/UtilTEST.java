import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String str = "--arg1";
 String expected = "arg1";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--arg";
 expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "arg";
 expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "arg--";
 expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "";
 expected = "";
 assertEquals(expected, Util.stripLeadingHyphens(str));
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"one\"";
 expected = "one";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"\"";
 expected = "";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "one two";
 expected = "one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"one two\" one two";
 expected = "one two one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
 }

 // Test for edge cases
 @Test
 public void testEdgeCases() {
 String str = "---arg--";
 String expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--arg--";
 expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--arg--";
 expected = "arg";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "\"\"one two\"\"";
 String expectedQuotes = "one two";
 String expectedNoQuotes = "one two";
 assertEquals(expectedNoQuotes, Util.stripLeadingAndTrailingQuotes(str));
 assertEquals(expectedQuotes, Util.stripLeadingAndTrailingQuotes(str));
 }

 // Test for error cases
 @Test(expected = IllegalArgumentException.class)
 public void testNullString() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringQuotes() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}