import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest
{

 @Test
 public void testStripLeadingHyphens()
 {
 // Test empty string
 assertEquals("", Util.stripLeadingHyphens(""));
 // Test no leading hyphens
 assertEquals("test", Util.stripLeadingHyphens("test"));
 // Test leading single hyphen
 assertEquals("test", Util.stripLeadingHyphens("-test"));
 // Test leading double hyphens
 assertEquals("test", Util.stripLeadingHyphens("--test"));
 // Test leading multiple hyphens
 assertEquals("test", Util.stripLeadingHyphens("--test--"));
 // Test null input
 assertEquals(null, Util.stripLeadingHyphens(null));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes()
 {
 // Test empty string
 assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
 // Test no quotes
 assertEquals("test", Util.stripLeadingAndTrailingQuotes("test"));
 // Test leading quotes
 assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test\""));
 // Test trailing quotes
 assertEquals("test", Util.stripLeadingAndTrailingQuotes("test\""));
 // Test both leading and trailing quotes
 assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test\""));
 }

 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesWithNull()
 {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}