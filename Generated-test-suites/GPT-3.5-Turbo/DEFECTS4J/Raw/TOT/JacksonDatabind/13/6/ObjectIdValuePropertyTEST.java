import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // typical use case
        String json = "\"test\"";
        JsonParser jp = new JsonParser(json);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(jp, ctxt, instance);
        assertNotNull(property);
        assertEquals(instance, property.deserializeSetAndReturn(jp, ctxt, instance));
        
        // edge case: null id
        json = "null";
        jp = new JsonParser(json);
        assertNull(property.deserializeSetAndReturn(jp, ctxt, instance));
        
        // error scenario: deserializer fails
        jp = new JsonParser(json);
        property.setValueDeserializer(new CustomDeserializer());
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Deserialization failed", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // typical use case
        Object instance = new Object();
        Object value = "test";
        assertEquals(instance, property.setAndReturn(instance, value));
        
        // error scenario: idProperty is null
        objectIdReader.idProperty = null;
        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}