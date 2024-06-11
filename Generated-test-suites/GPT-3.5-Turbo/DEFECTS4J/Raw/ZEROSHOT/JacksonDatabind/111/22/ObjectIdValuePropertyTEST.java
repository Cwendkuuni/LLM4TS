import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(src, property);
    }

    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        PropertyName newName = null;
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(src, property);
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        PropertyName newName = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty beanProperty = property.withName(newName);
        assertNotNull(beanProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty beanProperty = property.withValueDeserializer(deser);
        assertNotNull(beanProperty);
        assertEquals(property, beanProperty);
        assertEquals(deser, beanProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        NullValueProvider nva = null;
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        SettableBeanProperty beanProperty = property.withNullProvider(nva);
        assertNotNull(beanProperty);
        assertEquals(property, beanProperty);
        assertEquals(nva, beanProperty.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        property.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.setAndReturn(instance, value));
    }
}