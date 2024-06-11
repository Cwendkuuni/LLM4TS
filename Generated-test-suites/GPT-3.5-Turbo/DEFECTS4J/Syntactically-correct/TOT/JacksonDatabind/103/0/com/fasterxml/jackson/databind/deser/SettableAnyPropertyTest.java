import org.junit.*;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet_WithNullValue_ShouldSetNullValue() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Object instance = new Object();
        String propName = "testProp";

        // Act
        anyProperty.deserializeAndSet(null, null, instance, propName);

        // Assert
        assertNull(instance.get(propName));
    }

    @Test
    public void testDeserializeAndSet_WithNonNullValue_ShouldSetValue() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Object instance = new Object();
        String propName = "testProp";
        Object propValue = "testValue";

        // Act
        anyProperty.deserializeAndSet(null, null, instance, propName, propValue);

        // Assert
        assertEquals(propValue, instance.get(propName));
    }

    @Test
    public void testDeserialize_WithNullValue_ShouldReturnNull() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        JsonParser parser = null;
        DeserializationContext context = null;

        // Act
        Object result = anyProperty.deserialize(parser, context);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserialize_WithNonNullValue_ShouldReturnValue() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        JsonParser parser = null;
        DeserializationContext context = null;
        Object value = "testValue";

        // Act
        Object result = anyProperty.deserialize(parser, context, value);

        // Assert
        assertEquals(value, result);
    }

    @Test
    public void testSet_WithNullValue_ShouldNotThrowException() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Object instance = new Object();
        Object propName = "testProp";
        Object value = null;

        // Act + Assert
        anyProperty.set(instance, propName, value);
    }

    @Test
    public void testSet_WithNonNullValue_ShouldSetValue() throws IOException {
        // Arrange
        SettableAnyProperty anyProperty = new SettableAnyProperty(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Object instance = new Object();
        Object propName = "testProp";
        Object value = "testValue";

        // Act
        anyProperty.set(instance, propName, value);

        // Assert
        assertEquals(value, instance.get(propName));
    }
}