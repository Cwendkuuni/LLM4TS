import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens_NullString_ReturnNull() {
    String str = null;
    assertNull(Util.stripLeadingHyphens(str));
  }

  @Test
  public void testStripLeadingHyphens_StringWithDoubleHyphens_ReturnSubstring() {
    String str = "--example";
    assertEquals("example", Util.stripLeadingHyphens(str));
  }

  @Test
  public void testStripLeadingHyphens_StringWithSingleHyphen_ReturnSubstring() {
    String str = "-example";
    assertEquals("example", Util.stripLeadingHyphens(str));
  }

  @Test
  public void testStripLeadingHyphens_StringWithNoHyphen_ReturnSameString() {
    String str = "example";
    assertEquals("example", Util.stripLeadingHyphens(str));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_StringWithLeadingAndTrailingQuotes_ReturnSubstring() {
    String str = "\"example\"";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_StringWithLeadingQuotes_ReturnSubstring() {
    String str = "\"example";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_StringWithTrailingQuotes_ReturnSubstring() {
    String str = "example\"";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_StringWithNoQuotes_ReturnSameString() {
    String str = "example";
    assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
  }
}