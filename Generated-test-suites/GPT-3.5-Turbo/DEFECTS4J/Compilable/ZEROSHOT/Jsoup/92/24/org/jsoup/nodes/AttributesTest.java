
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("KEY2"));
        assertEquals("value3", attributes.getIgnoreCase("Key3"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));

        attributes.put("key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("Key4", "value4");
        assertEquals("value4", attributes.get("Key4"));

        attributes.putIgnoreCase("Key1", "newValue1");
        assertEquals("newValue1", attributes.get("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("key5", true);
        assertFalse(attributes.hasKey("key5"));

        attributes.put("key6", false);
        assertFalse(attributes.hasKey("key6"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("KEY2");
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
        assertFalse(attributes.hasKey("key4"));
        assertFalse(attributes.hasKey("KEY5"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertTrue(attributes.hasKeyIgnoreCase("KeY3"));
        assertFalse(attributes.hasKeyIgnoreCase("key4"));
        assertFalse(attributes.hasKeyIgnoreCase("key5"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());
        attributes.remove("key4");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key4", "value4");
        newAttributes.put("key5", "value5");
        newAttributes.put("key6", "value6");

        attributes.addAll(newAttributes);

        assertTrue(attributes.hasKey("key4"));
        assertTrue(attributes.hasKey("key5"));
        assertTrue(attributes.hasKey("key6"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        assertEquals(attributes.size(), count);
    }

    @Test
    public void testAsList() {
        assertEquals(attributes.size(), attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertTrue(attributes.dataset().containsKey("key1"));
        assertTrue(attributes.dataset().containsKey("key2"));
    }

    @Test
    public void testHtml() {
        String html = attributes.html();
        assertEquals(" key1=\"value1\" key2=\"value2\" key3=\"value3\"", html);
    }

    @Test
    public void testEquals() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");
        newAttributes.put("key3", "value3");

        assertEquals(attributes, newAttributes);
    }

    @Test
    public void testHashCode() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("key1", "value1");
        newAttributes.put("key2", "value2");
        newAttributes.put("key3", "value3");

        assertEquals(attributes.hashCode(), newAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.normalize();
        assertEquals("key1", attributes.asList().get(0).getKey());
        assertEquals("key2", attributes.asList().get(1).getKey());
        assertEquals("key3", attributes.asList().get(2).getKey());
    }
}
