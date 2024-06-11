
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate_typicalInput() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Typical input with a single decimal character
    String input = "&#65;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 3, result);
    Assert.assertEquals("Incorrect translation output", "A", writer.toString());
  }
  
  @Test
  public void testTranslate_typicalInputHex() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Typical input with a single hex character
    String input = "&#x41;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 4, result);
    Assert.assertEquals("Incorrect translation output", "A", writer.toString());
  }
  
  @Test
  public void testTranslate_typicalInputNoSemiColon() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

    // Typical input with optional semi-colon
    String input = "&#65";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 3, result);
    Assert.assertEquals("Incorrect translation output", "A", writer.toString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTranslate_errorInputMissingSemiColon() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

    // Input with missing semi-colon
    String input = "&#65";
    StringWriter writer = new StringWriter();
    unescaper.translate(input, 0, writer);
  }
  
  @Test(expected = NumberFormatException.class)
  public void testTranslate_errorInputInvalidNumber() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Input with invalid number
    String input = "&#invalid;";
    StringWriter writer = new StringWriter();
    unescaper.translate(input, 0, writer);
  }
  
  @Test
  public void testTranslate_edgeCaseUpperLimit() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Input with maximum entity value
    String input = "&#1114111;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 10, result);
    Assert.assertEquals("Incorrect translation output", "\uD83D\uDE00", writer.toString());
  }
  
  @Test
  public void testTranslate_edgeCaseLowerHexBoundary() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Input with minimum hex boundary
    String input = "&#x0;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 5, result);
    Assert.assertEquals("Incorrect translation output", "\u0000", writer.toString());
  }
  
  @Test
  public void testTranslate_edgeCaseUpperHexBoundary() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Input with maximum hex boundary
    String input = "&#xFF;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 5, result);
    Assert.assertEquals("Incorrect translation output", "\u00FF", writer.toString());
  }
  
  @Test
  public void testTranslate_edgeCaseSemiColonOnly() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Input with only semi-colon
    String input = "&#;";
    StringWriter writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    
    Assert.assertEquals("Incorrect translation result", 2, result);
    Assert.assertEquals("Incorrect translation output", "&", writer.toString());
  }
}
