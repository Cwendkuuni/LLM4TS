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
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "&#34;value&#34;");
        assertEquals("key", encodedAttribute.getKey());
        assertEquals("\"value\"", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("key", "differentValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals("test"));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("key", "differentValue");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}