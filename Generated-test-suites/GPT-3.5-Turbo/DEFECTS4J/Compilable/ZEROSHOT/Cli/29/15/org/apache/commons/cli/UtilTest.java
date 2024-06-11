package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

  @Test
  public void testStripLeadingHyphens_nullInput() {
    String input = null;
    String result = Util.stripLeadingHyphens(input);
    Assert.assertNull(result);
  }

  @Test
  public void testStripLeadingHyphens_doubleHyphens() {
    String input = "--test";
    String result = Util.stripLeadingHyphens(input);
    Assert.assertEquals("test", result);
  }

  @Test
  public void testStripLeadingHyphens_singleHyphen() {
    String input = "-test";
    String result = Util.stripLeadingHyphens(input);
    Assert.assertEquals("test", result);
  }

  @Test
  public void testStripLeadingHyphens_noHyphen() {
    String input = "test";
    String result = Util.stripLeadingHyphens(input);
    Assert.assertEquals("test", result);
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_startsWithQuotes() {
    String input = "\"one two\"";
    String result = Util.stripLeadingAndTrailingQuotes(input);
    Assert.assertEquals("one two", result);
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_endsWithQuotes() {
    String input = "one two\"";
    String result = Util.stripLeadingAndTrailingQuotes(input);
    Assert.assertEquals("one two", result);
  }

  @Test
  public void testStripLeadingAndTrailingQuotes_noQuotes() {
    String input = "one two";
    String result = Util.stripLeadingAndTrailingQuotes(input);
    Assert.assertEquals("one two", result);
  }

}