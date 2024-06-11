import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        // Edge case - key not found
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Typical use case
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        
        // Edge case - key with different case
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
        
        // Edge case - key not found
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    // More test cases for other methods...

}