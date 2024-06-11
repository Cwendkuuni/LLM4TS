package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "25");
        attributes.put("gender", "male");
    }
    
    @Test
    public void testGet() {
        assertEquals("John", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
        assertEquals("male", attributes.get("gender"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        assertEquals("John", attributes.getIgnoreCase("name"));
        assertEquals("25", attributes.getIgnoreCase("age"));
        assertEquals("male", attributes.getIgnoreCase("gender"));
        assertEquals("John", attributes.getIgnoreCase("NAME"));
        assertEquals("25", attributes.getIgnoreCase("AGE"));
        assertEquals("male", attributes.getIgnoreCase("GENDER"));
    }
    
    @Test
    public void testPut() {
        assertNull(attributes.get("height"));
        attributes.put("height", "180");
        assertEquals("180", attributes.get("height"));
    }
    
    @Test
    public void testPutBoolean() {
        assertNull(attributes.get("active"));
        attributes.put("active", true);
        assertEquals("true", attributes.get("active"));
        attributes.put("active", false);
        assertNull(attributes.get("active"));
    }
    
    @Test
    public void testPutAttribute() {
        assertNull(attributes.get("address"));
        Attribute address = new Attribute("address", "123 Main Street");
        attributes.put(address);
        assertEquals("123 Main Street", attributes.get("address"));
    }
    
    @Test
    public void testRemove() {
        assertEquals(3, attributes.size());
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        assertEquals(3, attributes.size());
        attributes.removeIgnoreCase("name");
        assertFalse(attributes.hasKey("name"));
        assertEquals(2, attributes.size());
    }
    
    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("name"));
        assertTrue(attributes.hasKey("age"));
        assertTrue(attributes.hasKey("gender"));
        assertFalse(attributes.hasKey("height"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("name"));
        assertTrue(attributes.hasKeyIgnoreCase("age"));
        assertTrue(attributes.hasKeyIgnoreCase("gender"));
        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
        assertTrue(attributes.hasKeyIgnoreCase("AGE"));
        assertTrue(attributes.hasKeyIgnoreCase("GENDER"));
        assertFalse(attributes.hasKeyIgnoreCase("height"));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("height", "180");
        otherAttributes.put("weight", "70");
        
        assertEquals(3, attributes.size());
        
        attributes.addAll(otherAttributes);
        
        assertEquals(5, attributes.size());
        assertEquals("180", attributes.get("height"));
        assertEquals("70", attributes.get("weight"));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    public void testAsList() {
        assertEquals(3, attributes.asList().size());
    }
    
    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals("", dataset.get("name"));
        assertEquals("", dataset.get("age"));
        assertEquals("", dataset.get("gender"));
        
        attributes.put("data-name", "John");
        attributes.put("data-age", "25");
        
        dataset = attributes.dataset();
        assertEquals("John", dataset.get("name"));
        assertEquals("25", dataset.get("age"));
        assertEquals("", dataset.get("gender"));
    }
    
    @Test
    public void testHtml() {
        assertEquals(" name=\"John\" age=\"25\" gender=\"male\"", attributes.html());
    }
    
    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("name", "John");
        otherAttributes.put("age", "25");
        otherAttributes.put("gender", "male");
        
        assertTrue(attributes.equals(otherAttributes));
        
        otherAttributes.put("height", "180");
        
        assertFalse(attributes.equals(otherAttributes));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = attributes.hashCode();
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("name", "John");
        otherAttributes.put("age", "25");
        otherAttributes.put("gender", "male");
        
        assertEquals(hashCode, otherAttributes.hashCode());
        
        otherAttributes.put("height", "180");
        
        assertNotEquals(hashCode, otherAttributes.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        
        assertTrue(attributes.equals(clone));
        assertNotSame(attributes, clone);
    }
}