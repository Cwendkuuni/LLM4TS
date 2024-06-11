import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }
    
    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyName("name"));
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, new PropertyName("newName"));
        assertEquals("newName", property.getName().getSimpleName());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyName("name"));
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = src.withName(newName);
        assertEquals(newName, property.getName());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyName("name"));
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, new PropertyName("name"));
        NullValueProvider nva = new NullValueProvider();
        ObjectIdValueProperty property = src.withNullProvider(nva);
        assertEquals(nva, property.getNullValueProvider());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(value));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail();
        } catch (IOException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        } catch (IOException e) {
            fail();
        }
    }
    
    @Test
    public void testSet() {
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.set(instance, null);
            fail();
        } catch (IOException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        try {
            property.setAndReturn(instance, null);
            fail();
        } catch (IOException e) {
            // Expected exception
        }
    }
}