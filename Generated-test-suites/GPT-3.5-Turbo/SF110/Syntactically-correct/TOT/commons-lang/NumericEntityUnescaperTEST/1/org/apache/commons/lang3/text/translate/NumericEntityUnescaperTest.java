import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.*;

import java.io.*;
import org.apache.commons.lang3.text.translate.*;

public class NumericEntityUnescaperTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testTranslate_withValidInput_returnsNumberOfCharsTranslated() throws IOException{
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65;";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidInput_returnsZero() throws IOException{
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#invalid;";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withNoSemicolonAndSemiColonRequiredOption_throwsException() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Assert
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Semi-colon required at end of numeric entity");

        // Act
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_withNoSemicolonAndSemiColonOptionalOption_doesNotThrowException() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withNoSemicolonAndErrorIfNoSemiColonOption_throwsException() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Assert
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Semi-colon required at end of numeric entity");

        // Act
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_withSemicolonRequired_doesNotTranslateIfNoSemicolonPresent() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withSemicolonOptional_translatesEvenIfNoSemicolonPresent() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withErrorIfNoSemiColon_translatesEvenIfNoSemicolonPresent() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexInput_returnsZero() throws IOException{
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#xZZ;";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withValidHexInput_returnsNumberOfCharsTranslated() throws IOException{
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#x41;";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withEntityValueGreaterThan65535_translatesToTwoChars() throws IOException{
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharArrayWriter writer = new CharArrayWriter();
        CharSequence input = "&#65536;";

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(10, result);
        assertEquals("𐀀", writer.toString());
    }

}