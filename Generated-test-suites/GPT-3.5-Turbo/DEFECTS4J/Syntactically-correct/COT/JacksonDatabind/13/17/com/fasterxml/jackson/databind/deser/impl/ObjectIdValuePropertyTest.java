import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testWithAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, instance);
            // Check that deserialization and setting has been executed without error
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertSame(instance, result);
            // Check that deserialization, setting, and returning has been executed without error
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
            // Check that setting has been executed without error
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty();
        Object instance = new Object();
        Object value = new Object();
        try {
            Object result = property.setAndReturn(instance, value);
            assertSame(value, result);
            // Check that setting and returning has been executed without error
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}