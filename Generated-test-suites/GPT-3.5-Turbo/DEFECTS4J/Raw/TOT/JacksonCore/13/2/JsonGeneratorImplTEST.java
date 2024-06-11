
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        // Arrange
        JsonGeneratorImpl generator = spy(JsonGeneratorImpl.class);
        String fieldName = "name";
        String value = "John";

        // Act
        generator.writeStringField(fieldName, value);

        // Assert
        verify(generator).writeFieldName(fieldName);
        verify(generator).writeString(value);
    }

    @Test
    public void testEnableQuoteFieldNamesFeature() {
        // Arrange
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);

        // Act
        generator.enable(Feature.QUOTE_FIELD_NAMES);

        // Assert
        assertEquals(false, generator._cfgUnqNames);
    }

    @Test
    public void testGetHighestEscapedChar() {
        // Arrange
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        generator._maximumNonEscapedChar = 127;

        // Act
        int highestEscapedChar = generator.getHighestEscapedChar();

        // Assert
        assertEquals(127, highestEscapedChar);
    }

    @Test
    public void testSetCharacterEscapes() {
        // Arrange
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        CharacterEscapes esc = mock(CharacterEscapes.class);

        // Act
        generator.setCharacterEscapes(esc);

        // Assert
        assertEquals(esc, generator._characterEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        // Arrange
        JsonGeneratorImpl generator = mock(JsonGeneratorImpl.class);
        SerializableString sep = mock(SerializableString.class);

        // Act
        generator.setRootValueSeparator(sep);

        // Assert
        assertEquals(sep, generator._rootValueSeparator);
    }
}
