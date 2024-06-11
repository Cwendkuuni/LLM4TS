import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 assertEquals("test", Util.stripLeadingHyphens(str));

 str = "test";
 assertNull(Util.stripLeadingHyphens(str));

 str = "--";
 assertEquals("", Util.stripLeadingHyphens(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"test\"";
 assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));

 str = "test";
 assertNull(Util.stripLeadingAndTrailingQuotes(str));

 str = "\"test\"";
 assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
 }
}