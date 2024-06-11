import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens_nullString() {
    String result = Util.stripLeadingHyphens(null);
    assertNull(result);
  }
  
  @Test
  public void testStripLeadingHyphens_doubleHyphens() {
    String result = Util.stripLeadingHyphens("--test");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingHyphens_singleHyphen() {
    String result = Util.stripLeadingHyphens("-test");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingHyphens_noHyphen() {
    String result = Util.stripLeadingHyphens("test");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_leadingQuote() {
    String result = Util.stripLeadingAndTrailingQuotes("\"test");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_trailingQuote() {
    String result = Util.stripLeadingAndTrailingQuotes("test\"");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_bothQuotes() {
    String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
    assertEquals("test", result);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes_noQuotes() {
    String result = Util.stripLeadingAndTrailingQuotes("test");
    assertEquals("test", result);
  }
  
}