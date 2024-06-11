
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        
        // Test empty attributes
        assertEquals("", attributes.get("key1"));
        
        // Test get existing attribute
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        // Test get non-existing attribute
        assertEquals("", attributes.get("key2"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test empty attributes
        assertEquals("", attributes.getIgnoreCase("key1"));

        // Test get existing attribute
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));

        // Test get non-existing attribute
        assertEquals("", attributes.getIgnoreCase("key2"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        // Test put new attribute
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));

        // Test replace existing attribute
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();

        // Test remove existing attribute
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        // Test remove non-existing attribute
        attributes.remove("key2");
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test remove existing attribute
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        assertFalse(attributes.hasKey("key1"));

        // Test remove non-existing attribute
        attributes.removeIgnoreCase("key2");
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();

        // Test has existing attribute
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        // Test has non-existing attribute
        assertFalse(attributes.hasKey("key2"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();

        // Test has existing attribute
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));

        // Test has non-existing attribute
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();

        // Test empty attributes
        assertEquals(0, attributes.size());

        // Test attributes with one element
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        
        // Test addAll with non-empty attributes
        attributes1.addAll(attributes2);
        assertTrue(attributes1.hasKey("key3"));
        assertEquals("value3", attributes1.get("key3"));
        
        // Test addAll with empty attributes
        attributes1.addAll(new Attributes());
        assertFalse(attributes1.hasKey("key3"));
    }
    
    @Test
    public void testIterator() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        // Test iterator
        int count = 0;
        for (Attribute attribute : attributes1) {
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testAsList() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        // Test asList
        assertEquals(2, attributes1.asList().size());
    }
    
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        
        // Test dataset with empty attributes
        assertEquals(0, attributes.dataset().size());

        // Test dataset with existing attributes
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
        
        // Test put dataset
        dataset.put("key3", "value3");
        assertTrue(attributes.hasKey("data-key3"));
        assertEquals("value3", attributes.get("data-key3"));
    }
    
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        
        // Test html with empty attributes
        assertEquals("", attributes.html());

        // Test html with existing attributes
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String html = attributes.html();
        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }
    
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        // Test equals with equal attributes
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));
        
        // Test equals with different attributes
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        assertFalse(attributes1.equals(attributes3));
    }
    
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        // Test hashCode with equal attributes
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        
        // Test hashCode with different attributes
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        // Test clone
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }

}
