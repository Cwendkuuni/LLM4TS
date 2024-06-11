package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Attributes parent;
    private Attribute attribute;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.get(0));
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
        assertEquals("newValue", parent.get(0));
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentKey = new Attribute("newKey", "value");
        Attribute differentValue = new Attribute("key", "newValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentKey));
        assertFalse(attribute.equals(differentValue));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();

        assertTrue(attribute.equals(cloneAttribute));
        assertFalse(attribute == cloneAttribute);
    }
}