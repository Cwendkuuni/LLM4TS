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

 str = "--  test";
 expected = "  test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test";
 expected = "test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test--";
 expected = "test";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);

 str = "test--test";
 expected = "testtest";
 result = Util.stripLeadingHyphens(str);
 assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "'\"one two\"'";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"one two\"";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "\"one two\"'";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);

 str = "one two";
 expected = "one two";
 result = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, result);
 }
}