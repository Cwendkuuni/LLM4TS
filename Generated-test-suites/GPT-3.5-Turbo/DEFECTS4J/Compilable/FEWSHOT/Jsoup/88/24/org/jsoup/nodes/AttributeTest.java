package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
    private Attribute attribute1;
    private Attribute attribute2;
    private Attributes parent;

    @Before
    public void setUp() {
        attribute1 = new Attribute("key1", "value1");
        attribute2 = new Attribute("key2", "value2", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("key1", attribute1.getKey());
        assertEquals("key2", attribute2.getKey());
    }

    @Test
    public void testSetKey() {
        attribute1.setKey("newKey");
        assertEquals("newKey", attribute1.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value1", attribute1.getValue());
        assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testSetValue() {
        attribute1.setValue("newValue");
        assertEquals("newValue", attribute1.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key1=\"value1\"", attribute1.html());
        assertEquals("key2=\"value2\"", attribute2.html());
    }

    @Test
    public void testToString() {
        assertEquals("key1=\"value1\"", attribute1.toString());
        assertEquals("key2=\"value2\"", attribute2.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        assertEquals("key", attribute.getKey());
        assertEquals("<value>", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute1.isDataAttribute());
        assertFalse(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute1.shouldCollapseAttribute(outputSettings));
        assertTrue(attribute2.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute1.isBooleanAttribute());
        assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute3 = new Attribute("key1", "value1");
        assertEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCode() {
        Attribute attribute3 = new Attribute("key1", "value1");
        assertEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute1.clone();
        assertEquals(attribute1, clonedAttribute);
    }
}