import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributesTest {
    
    private Attributes attributes;

    @Test
    public void testGet() {
        // Test when attributes is null
        assertNull(attributes.get("key"));
        
        // Test when attribute exists with the given key
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        // Test when attribute does not exist with the given key
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        // Test when attributes is null
        assertNull(attributes.getIgnoreCase("key"));
        
        // Test when attribute exists with the given key (case insensitive)
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        
        // Test when attribute does not exist with the given key (case insensitive)
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        // Test when attributes is null
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        
        // Test when attributes is not null
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        // Test when value is true
        attributes.put("key", true);
        assertEquals("key", attributes.get("key"));
        
        // Test when value is false
        attributes.put("key", false);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        // Test when attributes is null
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
        
        // Test when attributes is not null
        Attribute newAttribute = new Attribute("key", "newValue");
        attributes.put(newAttribute);
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        // Test when attributes is null
        attributes.remove("key");
        assertNull(attributes.get("key"));
        
        // Test when attributes is not null
        attributes.put("key", "value");
        attributes.remove("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        // Test when attributes is null
        attributes.removeIgnoreCase("key");
        assertNull(attributes.get("key"));
        
        // Test when attributes is not null
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        // Test when attributes is null
        assertFalse(attributes.hasKey("key"));
        
        // Test when attributes is not null and key exists
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        
        // Test when attributes is not null and key does not exist
        assertFalse(attributes.hasKey("nonExistingKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        // Test when attributes is null
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        
        // Test when attributes is not null and key exists (case insensitive)
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        
        // Test when attributes is not null and key does not exist (case insensitive)
        assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testSize() {
        // Test when attributes is null
        assertEquals(0, attributes.size());
        
        // Test when attributes is not null and has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        // Test when incoming attributes is empty
        Attributes incoming = new Attributes();
        attributes.addAll(incoming);
        assertEquals(0, attributes.size());
        
        // Test when incoming attributes has elements
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testIterator() {
        // Test when attributes is null
        assertFalse(attributes.iterator().hasNext());
        
        // Test when attributes is not null and has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.iterator().hasNext());
    }

    @Test
    public void testAsList() {
        // Test when attributes is null
        assertEquals(Collections.emptyList(), attributes.asList());
        
        // Test when attributes is not null and has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(attributes.asList().size(), attributes.size());
    }

    @Test
    public void testDataset() {
        // Test when attributes is null
        assertFalse(attributes.dataset().entrySet().iterator().hasNext());
        
        // Test when attributes is not null and has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.dataset().entrySet().iterator().hasNext());
    }

    @Test
    public void testHtml() {
        // Test when attributes is null
        assertEquals("", attributes.html());
        
        // Test when attributes is not null and has elements
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertFalse(attributes.html().isEmpty());
    }

    @Test
    public void testEquals() {
        // Test when attributes is null
        Attributes other = null;
        assertFalse(attributes.equals(other));
        
        // Test when attributes is not null but other is null
        attributes.put("key1", "value1");
        assertFalse(attributes.equals(other));
        
        // Test when attributes is not null and other is not null but they are not equal
        other = new Attributes();
        other.put("key2", "value2");
        assertFalse(attributes.equals(other));
        
        // Test when attributes is not null and equal to other
        other.put("key1", "value1");
        assertTrue(attributes.equals(other));
    }

    @Test
    public void testHashCode() {
        // Test when attributes is null
        assertEquals(0, attributes.hashCode());
        
        // Test when attributes is not null
        attributes.put("key1", "value1");
        assertNotEquals(0, attributes.hashCode());
    }

    @Test
    public void testClone() {
        // Test when attributes is null
        Attributes clone = attributes.clone();
        assertNull(clone);
        
        // Test when attributes is not null
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
    }
}