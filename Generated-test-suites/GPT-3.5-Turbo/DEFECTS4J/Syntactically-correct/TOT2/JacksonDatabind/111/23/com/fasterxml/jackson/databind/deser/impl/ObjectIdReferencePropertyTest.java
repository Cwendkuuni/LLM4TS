import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithName() {
        // Test with a new property name
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = property.withName(newName);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        // Test with a new value deserializer
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty();
        JsonDeserializer<?> deser = new CustomDeserializer();
        SettableBeanProperty result = property.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    // Additional test cases for other methods can be added here

}