import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        
        property.deserializeAndSet(parser, context, instance);
        
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSet_IdNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        
        parser.setValue(null);
        
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSet_ValidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        Object id = new Object();
        
        parser.setValue(id);
        
        assertNull(property.deserializeAndSet(parser, context, instance));
        assertEquals(instance, property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        
        property.setAndReturn(instance, null);
    }

    @Test
    public void testSetAndReturn_WithIdProperty() throws IOException {
        ObjectIdProperty idProperty = new ObjectIdProperty();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        idProperty.setIdProperty(property);
        property.setIdProperty(idProperty);
        
        Object instance = new Object();
        Object value = new Object();
        
        assertEquals(instance, property.setAndReturn(instance, value));
    }
}