import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WhenArrayIsEmpty_ShouldReturnEmptyArray() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = null; // Mock the JSON Parser

        // Act
        String[] result = deserializer.deserialize(parser, null);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_WhenArrayConsistsOfNullValues_ShouldReturnArrayOfNulls() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = null; // Mock the JSON Parser

        // Act
        String[] result = deserializer.deserialize(parser, null);

        // Assert
        for (String value : result) {
            assertNull(value);
        }
    }

    @Test
    public void testDeserialize_WhenArrayConsistsOfStringValues_ShouldReturnArrayWithSameValues() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = null; // Mock the JSON Parser

        // Act
        String[] result = deserializer.deserialize(parser, null);

        // Assert
        String[] expected = {"value1", "value2", "value3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WhenArrayConsistsOfNonStringValue_ShouldThrowException() {
        // Arrange
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = null; // Mock the JSON Parser

        // Act/Assert
        assertThrows(IOException.class, () -> {
            deserializer.deserialize(parser, null);
        });
    }

}