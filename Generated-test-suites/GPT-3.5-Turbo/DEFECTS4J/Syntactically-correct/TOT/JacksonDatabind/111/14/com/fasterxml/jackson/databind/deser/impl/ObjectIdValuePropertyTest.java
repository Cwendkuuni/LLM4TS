
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals("propertyName", property.propertyName);
    }

    @Test
    public void testWithIdType() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property.getIdType());
    }

    @Test
    public void testWithMetadata() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property.getMetadata());
    }

    @Test
    public void testWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        assertEquals("newName", property.withName(newName).getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        assertEquals(deser, property.withValueDeserializer(deser).getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nva = new NullValueProvider();
        assertEquals(nva, property.withNullProvider(nva).getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(Annotation.class));
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
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        assertNull(property.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        Object value = new Object();
        property.setAndReturn(instance, value);
    }
}

