import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        NullValueProvider nva = new NullValueProvider();
        property = (ObjectIdValueProperty) property.withNullProvider(nva);
        assertNotNull(property);
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        try {
            property.deserializeAndSet(parser, ctxt, new Object());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSet() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        try {
            property.set(new Object(), new Object());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        try {
            Object result = property.setAndReturn(new Object(), new Object());
            assertNotNull(result);
        } catch (IOException e) {
            fail();
        }
    }
}