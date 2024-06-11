import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_REQUIRED);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getMember());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(SomeAnnotationClass.class));
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> property.deserializeAndSet(parser, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertThrows(IOException.class, () -> property.deserializeSetAndReturn(parser, ctxt, instance));
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> property.set(instance, value));
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> property.setAndReturn(instance, value));
    }

}