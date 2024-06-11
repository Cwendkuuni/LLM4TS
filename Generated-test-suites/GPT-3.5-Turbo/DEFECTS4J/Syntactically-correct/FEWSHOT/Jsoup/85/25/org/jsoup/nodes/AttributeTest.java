
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
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
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentKeyAttribute = new Attribute("newKey", "value");
        Attribute differentValueAttribute = new Attribute("key", "newValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentKeyAttribute));
        assertFalse(attribute.equals(differentValueAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }
}
