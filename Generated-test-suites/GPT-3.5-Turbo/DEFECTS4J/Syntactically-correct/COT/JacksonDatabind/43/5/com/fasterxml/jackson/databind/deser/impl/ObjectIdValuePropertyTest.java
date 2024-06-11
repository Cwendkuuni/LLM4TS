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
        assertEquals(objectIdReader, property._objectIdReader);
        
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = src.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    // test getAnnotation method

    // test getMember method

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test when _valueDeserializer.deserialize returns null
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        assertNull(property.deserializeAndSet(p, ctxt, instance));
        
        // Test when _valueDeserializer.deserialize returns non-null value
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        property = property.withValueDeserializer(deser);
        assertNotNull(property.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test when _valueDeserializer.deserialize returns null
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
        
        // Test when _valueDeserializer.deserialize returns non-null value
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        property = property.withValueDeserializer(deser);
        assertNotNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        // Test when idProperty is null
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        try {
            property.set(instance, value);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }

        // Test when idProperty is not null
        SettableBeanProperty idProperty = new SettableBeanProperty();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        property = new ObjectIdValueProperty(objectIdReader, null);
        objectIdReader.idProperty = idProperty;
        assertEquals(value, property.setAndReturn(instance, value));
    }

}