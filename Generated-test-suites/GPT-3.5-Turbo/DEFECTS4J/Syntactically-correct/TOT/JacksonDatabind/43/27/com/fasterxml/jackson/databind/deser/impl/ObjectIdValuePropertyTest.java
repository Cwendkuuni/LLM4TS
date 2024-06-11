import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullId_ReturnsNull() {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), createPropertyMetadata());
        JsonParser parser = createJsonParserWithNullValue();
        DeserializationContext context = createDeserializationContext();
        Object instance = new Object();
        
        // Act
        Object result = property.deserializeAndSet(parser, context, instance);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet_NonNullId_ReturnsInstance() {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReader(), createPropertyMetadata());
        JsonParser parser = createJsonParserWithNonNullValue();
        DeserializationContext context = createDeserializationContext();
        Object instance = new Object();
        
        // Act
        Object result = property.deserializeAndSet(parser, context, instance);
        
        // Assert
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty_ThrowsUnsupportedOperationException() {
        // Arrange
        ObjectIdValueProperty property = new ObjectIdValueProperty(createObjectIdReaderWithoutIdProperty(), createPropertyMetadata());
        Object instance = new Object();
        Object value = new Object();
        
        // Act
        property.setAndReturn(instance, value);
        
        // Assert (exception expected)
    }


    // Helper methods

    private ObjectIdReader createObjectIdReader() {
        // return a mocked ObjectIdReader with the required configuration for testing
    }

    private ObjectIdReader createObjectIdReaderWithoutIdProperty() {
        // return a mocked ObjectIdReader without the idProperty for testing
    }

    private PropertyMetadata createPropertyMetadata() {
        // return a mocked PropertyMetadata object with the required configuration for testing
    }

    private JsonParser createJsonParserWithNullValue() {
        // return a mocked JsonParser with a null value for testing
    }

    private JsonParser createJsonParserWithNonNullValue() {
        // return a mocked JsonParser with a non-null value for testing
    }

    private DeserializationContext createDeserializationContext() {
        // return a mocked DeserializationContext with the required configuration for testing
    }
}