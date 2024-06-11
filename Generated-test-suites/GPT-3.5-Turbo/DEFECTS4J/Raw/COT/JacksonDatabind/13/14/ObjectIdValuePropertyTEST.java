import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertNull(property.getAnnotation(null));
        assertNull(property.getMember());
        assertNull(property.withName(null));
        assertNull(property.withValueDeserializer(null));
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        
        try {
            property.setAndReturn(instance, value);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}