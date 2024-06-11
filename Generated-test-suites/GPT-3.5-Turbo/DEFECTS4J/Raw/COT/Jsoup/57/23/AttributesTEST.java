
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("", attributes.get("key3"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");
        
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        attributes.put("key2", true);
        assertEquals("key2", attributes.get("key2"));
        
        attributes.put("key2", false);
        assertEquals("", attributes.get("key2"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        
        attributes.removeIgnoreCase("key2");
        assertFalse(attributes.hasKey("key2"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
        
        attributes.put("KEY3", "value3");
        assertTrue(attributes.hasKeyIgnoreCase("key3"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(2, attributes.size());
        
        attributes.remove("key1");
        assertEquals(1, attributes.size());
        
        attributes.removeIgnoreCase("key2");
        assertEquals(0, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");
        
        attributes1.addAll(attributes2);
        
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
    }
}
