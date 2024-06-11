import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_shouldReturnZero_whenInputIsEmpty() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("", 0, writer);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_shouldReturnZero_whenInputDoesNotStartWithNumericEntity() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("abc", 0, writer);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_shouldReturnZero_whenInputIsInvalidHexNumericEntity() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#xx;", 0, writer);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_shouldReturnZero_whenInputIsInvalidDecimalNumericEntity() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#x;", 0, writer);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testTranslate_shouldWriteEntityValueToWriter_whenInputIsValidHexNumericEntity() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#x41;", 0, writer);

        // Assert
        Assert.assertEquals(6, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_shouldWriteEntityValueToWriter_whenInputIsValidDecimalNumericEntity() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#65;", 0, writer);

        // Assert
        Assert.assertEquals(5, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_shouldWriteEntityValueToWriter_whenInputIsValidHexNumericEntityWithSemicolon() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#x41;;", 0, writer);

        // Assert
        Assert.assertEquals(7, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_shouldThrowException_whenSemicolonIsRequiredButNotPresent() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();

        // Act
        unescaper.translate("&#65;", 0, writer);
    }

    @Test
    public void testTranslate_shouldWriteEntityValueToWriter_whenSemicolonIsOptional() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#65;", 0, writer);

        // Assert
        Assert.assertEquals(5, result);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnZero_whenSemicolonIsRequiredButNotPresentAndErrorIfNoSemicolonIsSet() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate("&#65;", 0, writer);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testIsSet_shouldReturnTrue_whenOptionIsSet() {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        // Act & Assert
        Assert.assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_shouldReturnFalse_whenOptionIsNotSet() {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Act & Assert
        Assert.assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
}