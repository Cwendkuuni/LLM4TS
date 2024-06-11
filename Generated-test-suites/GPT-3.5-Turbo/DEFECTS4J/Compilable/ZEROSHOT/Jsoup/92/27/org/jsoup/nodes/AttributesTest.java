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
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
        assertEquals("value", attributes.getIgnoreCase("TEST"));
    }

    @Test
    public void testPut() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertEquals("", attributes.get("test"));

        attributes.put("test", false);
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attr = new Attribute("test", "value");
        attributes.put(attr);
        assertTrue(attributes.hasKey("test"));
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testRemove() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));

        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));

        attributes.removeIgnoreCase("TEST");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testHasKey() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertTrue(attributes.hasKeyIgnoreCase("TEST"));

        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        attributes.put("test3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testToString() {
        attributes.put("test", "value");
        assertEquals(" test=\"value\"", attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("test", "value");

        attributes.put("test", "value");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("test", "value");

        attributes.put("test", "value");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("test", "value");

        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
        assertNotSame(attributes, clone);
    }

    @Test
    public void testNormalize() {
        attributes.put("Test", "value");

        attributes.normalize();

        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testDataset() {
        attributes.put("data-test", "value");

        assertTrue(attributes.dataset().containsKey("test"));
        assertEquals("value", attributes.dataset().get("test"));
    }
}