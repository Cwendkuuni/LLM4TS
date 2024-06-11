
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

  private NumericEntityUnescaper numericEntityUnescaper;

  @Before
  public void setUp() {
    numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
  }

  @Test
  public void testTranslate_withValidNumericEntity_returnsEncodedCharacter() throws IOException {
    // Arrange
    String input = "&#65;";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(5, result);
    Assert.assertEquals("A", writer.toString());
  }

  @Test
  public void testTranslate_withInvalidNumericEntity_noSemicolonRequired_returnsUnchangedOutput() throws IOException {
    // Arrange
    String input = "&#65";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(0, result);
    Assert.assertEquals("", writer.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslate_withInvalidNumericEntity_errorIfNoSemicolon_returnsIllegalArgumentException() throws IOException {
    // Arrange
    numericEntityUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
    String input = "&#65";
    StringWriter writer = new StringWriter();

    // Act
    numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    // Expecting IllegalArgumentException
  }

  @Test
  public void testTranslate_withHexadecimalEntity_returnsEncodedCharacter() throws IOException {
    // Arrange
    String input = "&#x41;";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(6, result);
    Assert.assertEquals("A", writer.toString());
  }

  @Test
  public void testTranslate_withHexadecimalEntity_notEndingWithSemicolon_returnsUnchangedOutput() throws IOException {
    // Arrange
    String input = "&#x41";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(0, result);
    Assert.assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_withEntityOver65535_returnsEncodedSurrogatePair() throws IOException {
    // Arrange
    String input = "&#65536;";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(9, result);
    Assert.assertEquals("\uD800\uDC00", writer.toString());
  }

  @Test
  public void testTranslate_withEntityBelow65535_returnsEncodedCharacter() throws IOException {
    // Arrange
    String input = "&#64;";
    StringWriter writer = new StringWriter();

    // Act
    int result = numericEntityUnescaper.translate(input, 0, writer);

    // Assert
    Assert.assertEquals(6, result);
    Assert.assertEquals("@", writer.toString());
  }
}

