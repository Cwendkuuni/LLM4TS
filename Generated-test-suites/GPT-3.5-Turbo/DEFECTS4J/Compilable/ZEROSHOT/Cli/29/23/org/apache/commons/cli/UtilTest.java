package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    assertEquals("test", Util.stripLeadingHyphens("test"));
    assertEquals("test", Util.stripLeadingHyphens("-test"));
    assertEquals("test", Util.stripLeadingHyphens("--test"));
  }

  @Test
  public void testStripLeadingAndTrailingQuotes() {
    assertEquals("test", Util.stripLeadingAndTrailingQuotes("test"));
    assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test"));
    assertEquals("test", Util.stripLeadingAndTrailingQuotes("test\""));
    assertEquals("test", Util.stripLeadingAndTrailingQuotes("\"test\""));
  }

}