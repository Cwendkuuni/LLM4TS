import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslateWithHexEntity() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#x41;";
    int index = 0;
    StringWriter out = new StringWriter();
    unescaper.translate(input, index, out);
    String output = out.toString();
    assertEquals("A", output);
  }

  @Test
  public void testTranslateWithDecimalEntity() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#65;";
    int index = 0;
    StringWriter out = new StringWriter();
    unescaper.translate(input, index, out);
    String output = out.toString();
    assertEquals("A", output);
  }

  @Test
  public void testTranslateWithMissingSemicolon() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#65";
    int index = 0;
    StringWriter out = new StringWriter();
    unescaper.translate(input, index, out);
    String output = out.toString();
    assertEquals("A", output);
  }

  @Test
  public void testTranslateWithInvalidEntity() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    CharSequence input = "&#invalid;";
    int index = 0;
    StringWriter out = new StringWriter();
    unescaper.translate(input, index, out);
    String output = out.toString();
    assertEquals("", output);
  }

}