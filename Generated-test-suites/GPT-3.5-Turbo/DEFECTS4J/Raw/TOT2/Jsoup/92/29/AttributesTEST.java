package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        assertEquals("", attributes.get("nonExistingKey"));
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        
        attributes.put("Key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        attributes.put("key1", "updatedValue");
        assertEquals("updatedValue", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", true);
        assertEquals("", attributes.get("key1"));
        
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        
        assertEquals(0, attributes.size());
        
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attrib1 = new Attributes();
        Attributes attrib2 = new Attributes();
        
        attrib1.put("key1", "value1");
        attrib1.put("key2", "value2");
        
        attrib2.put("key2", "value2.1");
        attrib2.put("key3", "value3");
        
        attrib1.addAll(attrib2);
        
        assertEquals(3, attrib1.size());
        assertTrue(attrib1.hasKey("key3"));
    }

}