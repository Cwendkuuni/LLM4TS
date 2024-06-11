import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate_withValidInput_returnsCorrectResult() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    // Test valid input with decimal numeric entity
    input.append("This is a test &#65;");
    expectedOutput.append("This is a test A");
    int result = unescaper.translate(input, 13, null);
    Assert.assertEquals(expectedOutput.toString(), input.toString());
    Assert.assertEquals(17, result);

    // Test valid input with hexadecimal numeric entity
    input.setLength(0);
    expectedOutput.setLength(0);
    input.append("This is a test &#x41;");
    expectedOutput.append("This is a test A");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(expectedOutput.toString(), input.toString());
    Assert.assertEquals(17, result);
  }

  @Test
  public void testTranslate_withInvalidInput_returnsZero() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder();

    // Test invalid input with missing numeric entity end
    input.append("This is a test &#65");
    int result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);

    // Test invalid input with non-numeric entity value
    input.setLength(0);
    input.append("This is a test &#ABC;");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);

    // Test invalid input with non-numeric entity value in hexadecimal format
    input.setLength(0);
    input.append("This is a test &#xGG;");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);
  }

  @Test
  public void testTranslate_withIncompleteInput_returnsZero() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder();

    // Test incomplete input with missing leading '&'
    input.append("This is a test #65;");
    int result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);

    // Test incomplete input with missing '#' after '&'
    input.setLength(0);
    input.append("This is a test &65;");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);

    // Test incomplete input with missing numeric value
    input.setLength(0);
    input.append("This is a test &#;");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);

    // Test incomplete input with missing entity end
    input.setLength(0);
    input.append("This is a test &#65");
    result = unescaper.translate(input, 13, null);
    Assert.assertEquals(0, result);
  }
}