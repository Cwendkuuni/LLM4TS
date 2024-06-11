package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNotNull(attributeWithParent.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey("key2");
        assertEquals("key2", attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key1", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        attribute.setValue("value2");
        assertEquals("value2", attribute.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        Attribute attribute = new Attribute("key", "value");
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        Attribute booleanAttribute = new Attribute("checked", null);
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("checked", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("input", null, new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("data-key", "value", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}