import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(Object.class, 1); // Arbitrary class and kind for testing purposes
    }

    @Test
    public void testDeserialize_WhenStringIsEmpty_ShouldReturnNull() {
        // Arrange
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Act
        Object result = deserializer.deserialize(p, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_WhenStringIsWhitespace_ShouldReturnNull() {
        // Arrange
        JsonParser p = new JsonParser("   ");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Act
        Object result = deserializer.deserialize(p, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_WhenUnsupportedType_ShouldThrowIllegalArgumentException() {
        // Arrange
        JsonParser p = new JsonParser("test");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        deserializer = new FromStringDeserializer.Std(UnsupportedType.class, 1); // UnsupportedType is not in the types() method of FromStringDeserializer

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(p, ctxt));
    }

    @Test
    public void test_deserialize_WhenValueIsNull_ShouldReturnNull() {
        // Arrange
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Act
        Object result = deserializer._deserialize(null, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserialize_WhenValueIsEmpty_ShouldReturnNull() {
        // Arrange
        JsonParser p = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Act
        Object result = deserializer._deserialize("", ctxt);

        // Assert
        assertNull(result);
    }

    // More test cases for each specific type can be added here

    private static class UnsupportedType {
    }
}