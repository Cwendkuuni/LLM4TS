import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(property.withValueDeserializer(null)._valueDeserializer);
    }
    
    @Test
    public void testWithSameDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        assertSame(property.withValueDeserializer(deser), property);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        NullValueProvider nva = new NullValueProvider();
        assertNotNull(property.withNullProvider(nva)._nullProvider);
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Setup
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test
        assertDoesNotThrow(() -> property.deserializeAndSet(p, ctxt, instance));
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNullValue() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Setup
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNonNullValue() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Setup
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test
        assertNotNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSetWithNullIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Setup
        Object instance = new Object();
        Object value = new Object();

        // Test
        assertThrows(UnsupportedOperationException.class, () -> property.set(instance, value));
    }
    
    @Test
    public void testSetAndReturnWithNullIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Setup
        Object instance = new Object();
        Object value = new Object();

        // Test
        assertThrows(UnsupportedOperationException.class, () -> property.setAndReturn(instance, value));
    }
}