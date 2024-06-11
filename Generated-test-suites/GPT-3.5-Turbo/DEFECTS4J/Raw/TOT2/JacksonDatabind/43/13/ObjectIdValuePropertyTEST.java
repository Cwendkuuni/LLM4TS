import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNewName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty updatedProperty = property.withName(newName);
        assertEquals(newName, updatedProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        JsonDeserializer<?> newDeserializer = new MyJsonDeserializer();
        ObjectIdValueProperty updatedProperty = property.withValueDeserializer(newDeserializer);
        assertEquals(newDeserializer, updatedProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        try {
            property.deserializeAndSet(null, new DeserializationContext(), new Object());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        try {
            assertNull(property.deserializeSetAndReturn(null, new DeserializationContext(), new Object()));
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        try {
            property.set(null, new Object());
        } catch (UnsupportedOperationException e) {
            // Expected exception as idProperty is null
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), PropertyMetadata.STD_METADATA);
        try {
            property.setAndReturn(null, new Object());
        } catch (UnsupportedOperationException e) {
            // Expected exception as idProperty is null
        }
    }
}