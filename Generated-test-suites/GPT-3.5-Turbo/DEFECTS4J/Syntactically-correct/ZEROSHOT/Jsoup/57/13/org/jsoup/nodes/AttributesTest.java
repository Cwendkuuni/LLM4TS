import org.junit.*;
import static org.junit.Assert.*;

public class AttributesTest {
    
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("", attributes.getIgnoreCase("key3"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key2", false);
        
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertEquals("key1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }
    
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        
        attributes2.addAll(attributes1);
        
        assertTrue(attributes2.hasKey("key1"));
        assertTrue(attributes2.hasKey("key2"));
        assertEquals("value1", attributes2.get("key1"));
        assertEquals("value2", attributes2.get("key2"));
    }
    
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertNotNull(attributes.iterator());
    }
    
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(2, attributes.asList().size());
    }
    
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        assertEquals(2, attributes.dataset().size());
    }
    
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertNotNull(attributes.html());
    }
    
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        assertTrue(attributes1.equals(attributes2));
    }
    
    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertNotNull(attributes.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        Attributes clone = attributes.clone();
        
        assertTrue(attributes.equals(clone));
    }
    
    @Test
    public void testDataKey() {
        String key = "key";
        String dataKey = "data-key";
        
        assertEquals(dataKey, Attributes.dataKey(key));
    }
}