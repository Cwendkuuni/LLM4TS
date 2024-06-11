import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
        assertEquals(objectIdReader.propertyName, property.getPropertyName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }
    
    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()).withName(newName);
        assertNotNull(property);
        assertEquals(newName, property.getPropertyName());
    }
    
    @Test
    public void testWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        JsonDeserializer<?> deser = new CustomDeserializer();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()).withValueDeserializer(deser);
        assertNotNull(property);
        assertEquals(deser, property.getValueDeserializer());
    }
    
    // Add more test cases based on the edge cases and error scenarios identified
    
}