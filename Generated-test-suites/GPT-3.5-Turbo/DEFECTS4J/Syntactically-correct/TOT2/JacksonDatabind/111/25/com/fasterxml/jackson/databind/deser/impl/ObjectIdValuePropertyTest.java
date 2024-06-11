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
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        NullValueProvider nvp = new NullValueProvider();
        SettableBeanProperty newProperty = property.withNullProvider(nvp);

        assertEquals(nvp, newProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Mocking JsonParser, DeserializationContext, and instance
        JsonParser p = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> property.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Mocking JsonParser, DeserializationContext, and instance
        JsonParser p = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> property.deserializeSetAndReturn(p, ctxt, instance));
    }
}