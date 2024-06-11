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
    public void testWithNullName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        PropertyName newName = null;
        ObjectIdValueProperty newProperty = property.withName(newName);
        
        assertNotNull(newProperty);
    }

    @Test
    public void testWithNullDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonDeserializer<?> deser = null;
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);
        
        assertNotNull(newProperty);
    }

    @Test
    public void testDeserializeAndSet() {
        // Test deserializeAndSet method with valid arguments
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Test deserializeSetAndReturn method with valid arguments
    }

    @Test
    public void testSet() {
        // Test set method with valid arguments
    }

    @Test
    public void testSetAndReturn() {
        // Test setAndReturn method with valid arguments
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        property.setAndReturn(new Object(), "test");
    }

}