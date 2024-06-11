import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testWithName() {
        // Test with valid PropertyName
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty updatedProperty = property.withName(newName);
        
        assertEquals(newName, updatedProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        // Test with valid JsonDeserializer
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonDeserializer<?> deserializer = new CustomDeserializer();
        ObjectIdValueProperty updatedProperty = property.withValueDeserializer(deserializer);
        
        assertEquals(deserializer, updatedProperty.getValueDeserializer());
    }
    
    @Test
    public void testDeserializeAndSet() {
        // Test deserialization and setting
        // Add test case for edge case where id is null
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        // Test deserialization, setting and returning
        // Add test case for exception handling scenario
    }
    
    @Test
    public void testSet() {
        // Test setting
        // Add test case for edge case where idProperty is null
    }
    
    @Test
    public void testSetAndReturn() {
        // Test setting and returning
        // Add test case for exception handling scenario
    }

    // Add more test cases for other methods if needed
    
}