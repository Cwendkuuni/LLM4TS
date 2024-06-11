import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testConstructor2() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
    }

    @Test
    public void testConstructor3() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testConstructor4() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, "newName");

        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(new PropertyName("newName"), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);

        assertEquals(property._objectIdReader, newProperty._objectIdReader);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(property._objectIdReader, newProperty._objectIdReader);
        assertEquals(newName, newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getAnnotation(Class.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> property.deserializeAndSet(jp, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> property.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();

        assertThrows(UnsupportedOperationException.class, () -> property.set(instance, value));
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();

        assertThrows(UnsupportedOperationException.class, () -> property.setAndReturn(instance, value));
    }
}