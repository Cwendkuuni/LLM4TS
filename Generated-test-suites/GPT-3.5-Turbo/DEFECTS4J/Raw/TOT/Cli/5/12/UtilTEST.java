import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens() {
    // Typical use case
    String str1 = "--example";
    String expected1 = "example";
    assertEquals(expected1, Util.stripLeadingHyphens(str1));

    // Edge case: empty string
    String str2 = "";
    String expected2 = "";
    assertEquals(expected2, Util.stripLeadingHyphens(str2));

    // Edge case: hyphen at the end
    String str3 = "example-";
    String expected3 = "example-";
    assertEquals(expected3, Util.stripLeadingHyphens(str3));

    // Error scenario: null string
    String str4 = null;
    assertNull(Util.stripLeadingHyphens(str4));

    // Error scenario: string without hyphens
    String str5 = "example";
    assertEquals(str5, Util.stripLeadingHyphens(str5));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Typical use case
    String str1 = "\"one two\"";
    String expected1 = "one two";
    assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(str1));

    // Edge case: empty string
    String str2 = "";
    String expected2 = "";
    assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(str2));

    // Edge case: string without quotes
    String str3 = "example";
    assertEquals(str3, Util.stripLeadingAndTrailingQuotes(str3));

    // Error scenario: null string
    String str4 = null;
    assertNull(Util.stripLeadingAndTrailingQuotes(str4));

    // Error scenario: string without trailing quotes
    String str5 = "\"one two";
    assertEquals(str5, Util.stripLeadingAndTrailingQuotes(str5));
  }
}