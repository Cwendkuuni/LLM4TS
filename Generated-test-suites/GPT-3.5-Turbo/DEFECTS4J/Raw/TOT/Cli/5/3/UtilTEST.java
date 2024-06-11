import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

  // Test case for stripLeadingHyphens() method
  @Test
  public void testStripLeadingHyphens() {
    // Typical use case: String starts with double hyphens
    String input1 = "--testString";
    String expected1 = "testString";
    assertEquals(expected1, Util.stripLeadingHyphens(input1));

    // Typical use case: String starts with single hyphen
    String input2 = "-testString";
    String expected2 = "testString";
    assertEquals(expected2, Util.stripLeadingHyphens(input2));

    // Typical use case: String does not start with hyphen
    String input3 = "testString";
    String expected3 = "testString";
    assertEquals(expected3, Util.stripLeadingHyphens(input3));

    // Edge case: Empty string
    String input4 = "";
    String expected4 = "";
    assertEquals(expected4, Util.stripLeadingHyphens(input4));
  }

  // Test case for stripLeadingAndTrailingQuotes() method
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    // Typical use case: String starts and ends with quotes
    String input1 = "\"testString\"";
    String expected1 = "testString";
    assertEquals(expected1, Util.stripLeadingAndTrailingQuotes(input1));

    // Typical use case: String starts with quote, ends without quote
    String input2 = "\"testString";
    String expected2 = "\"testString";
    assertEquals(expected2, Util.stripLeadingAndTrailingQuotes(input2));

    // Typical use case: String starts without quote, ends with quote
    String input3 = "testString\"";
    String expected3 = "testString\"";
    assertEquals(expected3, Util.stripLeadingAndTrailingQuotes(input3));

    // Typical use case: String does not have quotes
    String input4 = "testString";
    String expected4 = "testString";
    assertEquals(expected4, Util.stripLeadingAndTrailingQuotes(input4));

    // Edge case: Empty string
    String input5 = "";
    String expected5 = "";
    assertEquals(expected5, Util.stripLeadingAndTrailingQuotes(input5));
  }
}