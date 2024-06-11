import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
    }

    @Test
    public void testWithDeser() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);

        assertEquals(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        
        // Test for exception handling scenario
        try {
            property.deserializeAndSet(null, null, new Object());
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());

        // Test for edge case with no idProperty
        try {
            property.setAndReturn(new Object(), new Object());
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected behavior
        }
    }
}