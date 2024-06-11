import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(objIdValueProperty);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nvp = new NullValueProvider();
        objIdValueProperty = (ObjectIdValueProperty) objIdValueProperty.withNullProvider(nvp);
        assertNotNull(objIdValueProperty);
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        // Setup the JsonParser and DeserializationContext
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertDoesNotThrow(() -> objIdValueProperty.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        // Setup the JsonParser and DeserializationContext
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertNull(objIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        assertThrows(UnsupportedOperationException.class, () -> objIdValueProperty.set(instance, value));
    }
}