package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value", new Attributes());
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
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
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
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value", new Attributes());
        assertEquals(attribute, attribute2);
        assertEquals(attribute.hashCode(), attribute2.hashCode());

        attribute2.setKey("newKey");
        assertNotEquals(attribute, attribute2);
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());

        attribute2.setKey("key");
        attribute2.setValue("newValue");
        assertNotEquals(attribute, attribute2);
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
    }
}