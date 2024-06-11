package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        attributes.put("style", "color: red");
        
        assertEquals("123", attributes.get("id"));
        assertEquals("test", attributes.get("class"));
        assertEquals("color: red", attributes.get("style"));
        assertEquals("", attributes.get("data-id"));
        assertEquals("", attributes.get("data-test"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        attributes.put("style", "color: red");
        
        assertEquals("123", attributes.getIgnoreCase("ID"));
        assertEquals("test", attributes.getIgnoreCase("CLASS"));
        assertEquals("color: red", attributes.getIgnoreCase("STYLE"));
        assertEquals("", attributes.getIgnoreCase("data-id"));
        assertEquals("", attributes.getIgnoreCase("data-test"));
    }
  
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        attributes.put("style", "color: red");

        assertEquals("123", attributes.get("id"));
        assertEquals("test", attributes.get("class"));
        assertEquals("color: red", attributes.get("style"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        attributes.put("disabled", false);
        
        assertTrue(attributes.hasKey("checked"));
        assertFalse(attributes.hasKey("disabled"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        attributes.remove("id");

        assertFalse(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
    }
  
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        
        assertTrue(attributes.hasKey("id"));
        assertFalse(attributes.hasKey("class"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");

        assertTrue(attributes.hasKeyIgnoreCase("ID"));
        assertFalse(attributes.hasKeyIgnoreCase("CLASS"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        
        assertEquals(2, attributes.size());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("id", "123");
        attributes.put("class", "test");
        
        Attributes clone = attributes.clone();
        
        assertEquals(attributes, clone);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        attributes.put("CLASS", "test");
        
        attributes.normalize();
        
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
    }
}