package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";
        
        attributes.put(key, value);
        
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testGetIgnoreCase() {
        String key = "Key";
        String value = "value";
        
        attributes.put(key, value);
        
        assertEquals(value, attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        String key = "key";
        String value = "value";
        
        attributes.put(key, value);
        
        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testRemove() {
        String key = "key";
        String value = "value";
        
        attributes.put(key, value);
        attributes.remove(key);
        
        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testHasKey() {
        String key = "key";
        
        attributes.put(key, "value");
        
        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBoolean() {
        String key = "key";
        
        attributes.put(key, true);

        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testAttributeEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testAttributeHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        
        assertNotSame(attributes, clone);
        assertEquals(attributes.size(), clone.size());
    }

}