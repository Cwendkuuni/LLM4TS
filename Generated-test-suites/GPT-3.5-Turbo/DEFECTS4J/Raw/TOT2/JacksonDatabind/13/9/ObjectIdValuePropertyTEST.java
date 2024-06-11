import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);
        
        assertEquals(reader.propertyName, property.getName());
        assertEquals(reader.getIdType(), property.getType());
        assertEquals(reader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);

        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithDeserializer() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);

        JsonDeserializer<?> newDeserializer = new CustomDeserializer();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);

        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    // Similar test methods for other public methods

    // Additional tests for edge cases and error scenarios

}