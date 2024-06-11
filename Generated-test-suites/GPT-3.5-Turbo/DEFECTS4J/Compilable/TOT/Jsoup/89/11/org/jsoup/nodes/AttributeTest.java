package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

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
        assertEquals("value", attribute.setValue("newValue"));
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
    public void testEquals_SameObject() {
        assertTrue(attribute.equals(attribute));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(attribute.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(attribute.equals("string"));
    }

    @Test
    public void testEquals_DifferentKey() {
        Attribute other = new Attribute("differentKey", "value");
        assertFalse(attribute.equals(other));
    }

    @Test
    public void testEquals_DifferentValue() {
        Attribute other = new Attribute("key", "differentValue");
        assertFalse(attribute.equals(other));
    }

    @Test
    public void testEquals_SameKeyAndValue() {
        Attribute other = new Attribute("key", "value");
        assertTrue(attribute.equals(other));
    }

    @Test
    public void testHashCode() {
        Attribute other = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}