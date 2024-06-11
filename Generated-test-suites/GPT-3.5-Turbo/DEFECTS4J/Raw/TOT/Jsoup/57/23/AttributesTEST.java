
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    // Test case for get(String key) method
    @Test
    public void testGet() {
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
        assertEquals("", attributes.get("age"));
    }
    
    // Test case for getIgnoreCase(String key) method
    @Test
    public void testGetIgnoreCase() {
        attributes.put("name", "John");
        assertEquals("John", attributes.getIgnoreCase("name"));
        assertEquals("", attributes.getIgnoreCase("age"));
    }
    
    // Test case for put(String key, String value) method
    @Test
    public void testPut() {
        attributes.put("name", "John");
        assertEquals("John", attributes.get("name"));
    }
    
    // Test case for put(String key, boolean value) method
    @Test
    public void testPutBoolean() {
        attributes.put("isAdult", true);
        assertEquals("true", attributes.get("isAdult"));
        
        attributes.put("isAdult", false);
        assertEquals("", attributes.get("isAdult"));
    }
    
    // Test case for put(Attribute attribute) method
    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("name", "John");
        attributes.put(attribute);
        assertEquals("John", attributes.get("name"));
    }
    
    // Test case for remove(String key) method
    @Test
    public void testRemove() {
        attributes.put("name", "John");
        attributes.remove("name");
        assertEquals("", attributes.get("name"));
    }
    
    // Test case for removeIgnoreCase(String key) method
    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("name", "John");
        attributes.removeIgnoreCase("NAME");
        assertEquals("", attributes.get("name"));
    }
    
    // Test case for hasKey(String key) method
    @Test
    public void testHasKey() {
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("age"));
    }
    
    // Test case for hasKeyIgnoreCase(String key) method
    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("name", "John");
        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
        assertFalse(attributes.hasKeyIgnoreCase("age"));
    }
    
    // Test case for size() method
    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put("name", "John");
        assertEquals(1, attributes.size());
    }
    
    // Test case for addAll(Attributes incoming) method
    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("name", "John");
        incoming.put("age", "25");
        
        attributes.addAll(incoming);
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
    }
    
    // Test case for iterator() method
    @Test
    public void testIterator() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("name", iterator.next().getKey());
        assertEquals("age", iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }
    
    // Test case for asList() method
    @Test
    public void testAsList() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        List<Attribute> attributeList = attributes.asList();
        assertEquals("John", attributeList.get(0).getValue());
        assertEquals("age", attributeList.get(1).getKey());
    }
    
    // Test case for dataset() method
    @Test
    public void testDataset() {
        attributes.put("data-name", "John");
        attributes.put("data-age", "25");
        
        Map<String, String> dataset = attributes.dataset();
        assertEquals("John", dataset.get("name"));
        assertEquals("25", dataset.get("age"));
    }
    
    // Test case for html() method
    @Test
    public void testHtml() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        String html = attributes.html();
        assertTrue(html.contains("name=\"John\""));
        assertTrue(html.contains("age=\"25\""));
    }
    
    // Test case for equals(Object o) method
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        
        Attributes attributes3 = new Attributes();
        attributes3.put("name", "Mike");
        
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }
    
    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        
        Attributes attributes3 = new Attributes();
        attributes3.put("name", "Mike");
        
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }
    
    // Test case for clone() method
    @Test
    public void testClone() {
        attributes.put("name", "John");
        Attributes clone = attributes.clone();
        assertEquals(attributes.get("name"), clone.get("name"));
    }
}
