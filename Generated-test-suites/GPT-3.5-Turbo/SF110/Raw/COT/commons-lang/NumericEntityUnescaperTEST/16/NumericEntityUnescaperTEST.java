
import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

  @Test
  public void testIsSet() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
  }

  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#65;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(5, result);
    assertEquals("A", writer.toString());
  }

  @Test
  public void testTranslateInvalidHex() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#xG;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateSemicolonRequired() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    CharSequence input = "&#65";
    StringWriter writer = new StringWriter();
    unescaper.translate(input, 0, writer);
  }

  @Test
  public void testTranslateSemicolonOptional() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
    CharSequence input = "&#65";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(4, result);
    assertEquals("A", writer.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateErrorIfNoSemicolon() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
    CharSequence input = "&#65";
    StringWriter writer = new StringWriter();
    unescaper.translate(input, 0, writer);
  }
}
