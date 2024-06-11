import org.junit.Assert;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Assert.assertNull(property.withValueDeserializer(null));
    }

    @Test
    public void testWithNonNullValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        
        Assert.assertNotNull(property.withValueDeserializer(deserializer));
        Assert.assertEquals(deserializer, property.withValueDeserializer(deserializer).getValueDeserializer());
    }

    @Test
    public void testWithNullNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Assert.assertNull(property.withNullProvider(null));
    }

    @Test
    public void testWithNonNullNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nva = new NullValueProvider();
        
        Assert.assertNotNull(property.withNullProvider(nva));
        Assert.assertEquals(nva, property.withNullProvider(nva).getNullValueProvider());
    }

    @Test
    public void testDeserializeAndSetWithNullJsonParser() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            property.deserializeAndSet(null, ctxt, instance);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null JsonParser", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeAndSetWithNullInstance() {
    	ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DeserializationContext ctxt = new DeserializationContext();
        JsonParser p = new JsonParser();
        
        try {
            property.deserializeAndSet(p, ctxt, null);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null instance", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNullJsonParser() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        try {
            property.deserializeSetAndReturn(null, ctxt, instance);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null JsonParser", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNullInstance() {
    	ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DeserializationContext ctxt = new DeserializationContext();
        JsonParser p = new JsonParser();
        
        try {
            property.deserializeSetAndReturn(p, ctxt, null);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null instance", e.getMessage());
        }
    }
    
    @Test
    public void testSetWithNullInstance() {
    	ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object value = new Object();
        
        try {
            property.set(null, value);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null instance", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturnWithNullInstance() {
    	ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object value = new Object();
        
        try {
            property.setAndReturn(null, value);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Null instance", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturnWithNullIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();

        try {
            property.setAndReturn(instance, value);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}