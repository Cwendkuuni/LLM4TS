package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("", "");
        attribute.setKey("");
        attribute.setValue("");
        attribute.parent = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(attribute);
        assertEquals("", attribute.getKey());
        assertEquals("", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("key");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("\"\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("\"\"", attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute otherAttribute = new Attribute("", "");
        assertTrue(attribute.equals(otherAttribute));
        assertTrue(attribute.equals(attribute));
        assertFalse(attribute.equals(null));

        otherAttribute = new Attribute("key", "");
        assertFalse(attribute.equals(otherAttribute));

        otherAttribute = new Attribute("", "value");
        assertFalse(attribute.equals(otherAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute otherAttribute = new Attribute("", "");
        assertEquals(attribute.hashCode(), otherAttribute.hashCode());

        otherAttribute = new Attribute("key", "");
        assertNotEquals(attribute.hashCode(), otherAttribute.hashCode());

        otherAttribute = new Attribute("", "value");
        assertNotEquals(attribute.hashCode(), otherAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
        assertNotSame(attribute, clonedAttribute);
    }
}