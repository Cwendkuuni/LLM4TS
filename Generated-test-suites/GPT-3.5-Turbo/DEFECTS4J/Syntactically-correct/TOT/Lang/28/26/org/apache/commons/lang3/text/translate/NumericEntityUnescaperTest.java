import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    private Writer writer;
    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setup() {
        writer = mock(Writer.class);
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withValidNumericEntity_shouldTranslateCodepoint() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        int index = 0;

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        verify(writer).write(65);
        assertEquals(4, result);
    }

    @Test
    public void testTranslate_withHexNumericEntity_shouldTranslateCodepoint() throws IOException {
        // Arrange
        CharSequence input = "&#x41;";
        int index = 0;

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        verify(writer).write(65);
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldReturnZero() throws IOException {
        // Arrange
        CharSequence input = "&#A;";
        int index = 0;

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        verifyZeroInteractions(writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withValidNumericEntityInMiddleOfString_shouldTranslateCodepoint() throws IOException {
        // Arrange
        CharSequence input = "This is a test &#x42; string";
        int index = 12;

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        verify(writer).write(66);
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_withInvalidNumericEntityInMiddleOfString_shouldReturnZero() throws IOException {
        // Arrange
        CharSequence input = "This is a test &#xG; string";
        int index = 12;

        // Act
        int result = numericEntityUnescaper.translate(input, index, writer);

        // Assert
        verifyZeroInteractions(writer);
        assertEquals(0, result);
    }
}