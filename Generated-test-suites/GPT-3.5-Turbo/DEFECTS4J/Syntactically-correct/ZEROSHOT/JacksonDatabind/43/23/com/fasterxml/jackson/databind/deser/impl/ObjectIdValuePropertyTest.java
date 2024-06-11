import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Test null instance
        property.deserializeAndSet(null, null, null);

        // Test normal case
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        property.deserializeAndSet(p, ctxt, instance);

        // Additional test cases...

        // Check the result
        // ...

    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Test null instance
        assertNull(property.deserializeSetAndReturn(null, null, null));

        // Test normal case
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        assertNotNull(property.deserializeSetAndReturn(p, ctxt, instance));

        // Additional test cases...

        // Check the result
        // ...

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.set(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.setAndReturn(null, null);
    }

    // Additional test methods...

}