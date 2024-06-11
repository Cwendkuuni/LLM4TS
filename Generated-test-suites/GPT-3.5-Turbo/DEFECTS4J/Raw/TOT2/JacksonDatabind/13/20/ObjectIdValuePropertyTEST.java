import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_METADATA);
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, newName);
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, newName);
        assertNotNull(objectIdValueProperty.withName(newName));
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        JsonDeserializer<?> deserializer = new JsonDeserializer();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_METADATA);
        assertNotNull(objectIdValueProperty.withValueDeserializer(deserializer));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STANDARD_METADATA);
        assertNull(objectIdValueProperty.getAnnotation(NonNull.class));
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STANDARD_METADATA);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> objectIdValueProperty.deserializeAndSet(jp, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STANDARD_METADATA);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STANDARD_METADATA);
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> objectIdValueProperty.set(instance, value));
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STANDARD_METADATA);
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> objectIdValueProperty.setAndReturn(instance, value));
    }
}