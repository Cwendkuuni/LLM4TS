package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens() {
    String str1 = Util.stripLeadingHyphens("--test");
    assertEquals("test", str1);

    String str2 = Util.stripLeadingHyphens("-test");
    assertEquals("test", str2);

    String str3 = Util.stripLeadingHyphens("test");
    assertEquals("test", str3);
  }

  @Test
  public void testStripLeadingAndTrailingQuotes() {
    String str1 = Util.stripLeadingAndTrailingQuotes("\"one two\"");
    assertEquals("one two", str1);

    String str2 = Util.stripLeadingAndTrailingQuotes("\"one two");
    assertEquals("\"one two", str2);

    String str3 = Util.stripLeadingAndTrailingQuotes("one two\"");
    assertEquals("one two\"", str3);

    String str4 = Util.stripLeadingAndTrailingQuotes("one two");
    assertEquals("one two", str4);
  }

}