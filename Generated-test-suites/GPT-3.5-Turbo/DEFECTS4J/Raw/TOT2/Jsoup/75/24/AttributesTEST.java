import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Create Attributes instance
        Attributes attributes = new Attributes();
        
        // Test for case where key exists
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        // Test for case where key does not exist
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Create Attributes instance
        Attributes attributes = new Attributes();
        
        // Test for case where key exists with different case
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        
        // Test for case where key does not exist
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        // Create Attributes instance
        Attributes attributes = new Attributes();
        
        // Test for adding a new attribute
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        // Test for replacing an existing attribute
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        
    }

    @Test
    public void testPutBoolean() {
        // Create Attributes instance
        Attributes attributes = new Attributes();
        
        // Test for adding a new boolean attribute
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        
        // Test for removing a boolean attribute
        attributes.put("key1", false);
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    // Add more test cases for other methods in Attributes class

}