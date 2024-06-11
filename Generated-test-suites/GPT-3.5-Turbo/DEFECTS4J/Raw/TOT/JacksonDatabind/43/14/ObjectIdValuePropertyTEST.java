import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdValueProperty property;
    
    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(objectIdReader, metadata);
        property.setDeser(new JsonDeserializer<>());
    }
    
    @Test
    public void testWithName() {
        PropertyName propertyName = new PropertyName("id");
        ObjectIdValueProperty newProperty = property.withName(propertyName);
        assertEquals(propertyName, newProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        // Test case for valid id
        assertNotNull(property.deserializeAndSet(p, ctxt, instance));
        // Test case for null id
        assertNull(property.deserializeAndSet(p, ctxt, instance));
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object id = new Object();
        // Test case for valid id
        assertEquals(instance, property.deserializeSetAndReturn(p, ctxt, instance));
        // Test case for null id
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        property.setAndReturn(instance, value);
    }
}