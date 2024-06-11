import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {
    
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key2", "value2");
        
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        attributes.put("key1", "updatedValue");
        assertEquals("updatedValue", attributes.get("key1"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        
        assertFalse(attributes.hasKey("key1"));
        
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
        
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKeyIgnoreCase("booleanKey"));
    }
    
    // Additional test cases for other methods should be added
    
    // Add edge cases and exception handling scenarios
    
}