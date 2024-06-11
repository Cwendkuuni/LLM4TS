package org.jsoup.nodes;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
    public void testPutAndGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanAttr", true);
        assertTrue(attributes.hasKey("booleanAttr"));
    }

    @Test
    public void testPutBooleanAttributeFalse() {
        attributes.put("booleanAttr", false);
        assertFalse(attributes.hasKey("booleanAttr"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("Key", "value");
        assertEquals("value", attributes.get("Key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.putIgnoreCase("Key", "value");
        attributes.removeIgnoreCase("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistingKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.putIgnoreCase("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        attributes.addAll(otherAttributes);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        assertEquals(2, attributes.dataset().size());
        assertTrue(attributes.dataset().containsKey("key1"));
        assertTrue(attributes.dataset().containsKey("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");

        attributes.put("key", "value");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key", "value");

        attributes.put("key", "value");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();

        // Assert that the cloned object is a different instance
        assertNotSame(attributes, clonedAttributes);

        // Assert that the cloned object has the same values
        assertEquals(attributes.get("key"), clonedAttributes.get("key"));
    }

    @Test
    public void testNormalize() {
        attributes.put("Key", "value");
        attributes.normalize();
        assertTrue(attributes.hasKey("key"));
    }
}