import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributesTest {
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
        assertEquals("", attributes.get("city"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertEquals("John", attributes.getIgnoreCase("nAmE"));
        assertEquals("25", attributes.getIgnoreCase("AgE"));
        assertEquals("", attributes.getIgnoreCase("city"));
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
        assertFalse(attributes.hasKey("city"));
        
        attributes.put("city", "New York");
        assertTrue(attributes.hasKey("city"));
        assertEquals("New York", attributes.get("city"));
        
        attributes.put("age", "30");
        assertEquals("30", attributes.get("age"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("boolean", true);
        
        assertTrue(attributes.hasKey("boolean"));
        assertEquals("true", attributes.get("boolean"));
        
        attributes.put("boolean", false);
        assertFalse(attributes.hasKey("boolean"));
        
        attributes.put("boolean", true);
        assertTrue(attributes.hasKey("boolean"));
        assertEquals("true", attributes.get("boolean"));
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        
        attributes.remove("age");
        assertFalse(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("age"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        
        attributes.removeIgnoreCase("nAmE");
        assertFalse(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        
        attributes.removeIgnoreCase("AGE");
        assertFalse(attributes.hasKey("name"));
        assertFalse(attributes.hasKey("age"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        assertFalse(attributes.hasKey("city"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        assertTrue(attributes.hasKeyIgnoreCase("nAmE"));
        assertTrue(attributes.hasKeyIgnoreCase("AgE"));
        assertFalse(attributes.hasKeyIgnoreCase("city"));
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals(2, attributes.size());
        
        attributes.remove("age");
        assertEquals(1, attributes.size());
        
        attributes.remove("name");
        assertEquals(0, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("city", "New York");
        
        assertEquals(2, attributes1.size());
        assertFalse(attributes1.hasKey("city"));
        
        attributes1.addAll(attributes2);
        
        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("city"));
        assertEquals("New York", attributes1.get("city"));
    }
    
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        List<Attribute> attributeList = attributes.asList();
        
        assertEquals(2, attributeList.size());
        assertEquals("John", attributeList.get(0).getValue());
        assertEquals("age", attributeList.get(1).getKey());
    }
    
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.put("data-city", "New York");
        attributes.put("data-country", "USA");
        
        Map<String, String> dataset = attributes.dataset();
        
        assertEquals(2, dataset.size());
        assertEquals("New York", dataset.get("city"));
        assertEquals("USA", dataset.get("country"));
    }
    
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        
        String html = attributes.html();
        
        assertEquals(" name=\"John\" age=\"25\"", html);
    }
    
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");
        
        assertEquals(attributes1, attributes2);
    }
    
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        attributes2.put("age", "25");
        
        assertTrue(attributes1.hashCode() == attributes2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        attributes1.put("age", "25");
        
        Attributes attributes2 = attributes1.clone();
        
        assertEquals(attributes1, attributes2);
    }
}