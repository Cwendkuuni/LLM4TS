import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty prop = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertSame(objectIdReader.propertyName, prop.propertyName);
        assertSame(objectIdReader.getIdType(), prop.getType());
        assertSame(metadata, prop.getMetadata());
        assertSame(objectIdReader.getDeserializer(), prop.getValueDeserializer());
        assertSame(objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, deser);
        
        assertSame(src.propertyName, prop.propertyName);
        assertSame(src.getType(), prop.getType());
        assertSame(src.getMetadata(), prop.getMetadata());
        assertSame(deser, prop.getValueDeserializer());
        assertSame(src._objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testCopyConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        String newName = "newName";
        PropertyName propertyName = new PropertyName(newName);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty prop = new ObjectIdValueProperty(src, propertyName);
        
        assertSame(propertyName, prop.propertyName);
        assertSame(src.getType(), prop.getType());
        assertSame(src.getMetadata(), prop.getMetadata());
        assertSame(src.getValueDeserializer(), prop.getValueDeserializer());
        assertSame(src._objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        String newName = "newName";
        PropertyName propertyName = new PropertyName(newName);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty prop = src.withName(propertyName);
        
        assertSame(propertyName, prop.propertyName);
        assertSame(src.getType(), prop.getType());
        assertSame(src.getMetadata(), prop.getMetadata());
        assertSame(src.getValueDeserializer(), prop.getValueDeserializer());
        assertSame(src._objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty prop = src.withValueDeserializer(deser);
        
        assertSame(src.propertyName, prop.propertyName);
        assertSame(src.getType(), prop.getType());
        assertSame(src.getMetadata(), prop.getMetadata());
        assertSame(deser, prop.getValueDeserializer());
        assertSame(src._objectIdReader, prop._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(prop.getAnnotation(Object.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(prop.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        prop.deserializeAndSet(jp, ctxt, instance);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        prop.deserializeSetAndReturn(jp, ctxt, instance);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testSet() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        
        try {
            prop.set(instance, new Object());
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Test passes if UnsupportedOperationException is thrown
        } catch (IOException e) {
            fail("Expected UnsupportedOperationException to be thrown");
        }
    }
    
    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty prop = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        
        try {
            prop.setAndReturn(instance, new Object());
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Test passes if UnsupportedOperationException is thrown
        } catch (IOException e) {
            fail("Expected UnsupportedOperationException to be thrown");
        }
    }
}