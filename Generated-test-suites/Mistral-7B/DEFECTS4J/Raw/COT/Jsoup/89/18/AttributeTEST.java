package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for Attribute.
 */
public class AttributeTest {

    /**
     * Test constructor Attribute(String key, String value).
     */
    @Test
    public void testConstructorStringString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals("key=value", attribute.html());
    }

    /**
     * Test constructor Attribute(String key, String value, Attributes parent).
     */
    @Test
    public void testConstructorStringStringAttributes() {
        Attributes parent = new Attributes();
        parent.put("key1", "value1");
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals("key=\"value\"", attribute.html());
        assertEquals("value1", parent.get("key1"));
        assertSame(parent, attribute.parent);
    }

    /**
     * Test method getKey().
     */
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    /**
     * Test method setKey().
     */
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    /**
     * Test method getValue().
     */
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    /**
     * Test method setValue().
     */
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        String newValue = "value2";
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals("value1", attribute.parent.get("key"));
    }

    /**
     * Test method html().
     */
    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        Attributes parent = new Attributes();
        parent.put("key1", "value1");
        attribute = new Attribute("key", "value", parent);
        StringBuilder sb2 = StringUtil.borrowBuilder();
        attribute.html(sb2, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));
        assertEquals("value1", parent.get("key1"));

        attribute.setValue("newValue");
        StringBuilder sb3 = StringUtil.borrowBuilder();
        attribute.html(sb3, new Document("").outputSettings());
        assertEquals("key=\"newValue\"", StringUtil.releaseBuilder(sb3));
        assertEquals("newValue", attribute.getValue());
        assertEquals("value1", parent.get("key1"));
    }

    /**
     * Test method equals().
     */
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value1");
        Attribute attribute4 = new Attribute("key1", null);
        Attribute attribute5 = new Attribute("key1", "value2");

        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
    }

    /**
     * Test method hashCode().
     */
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value1");
        Attribute attribute4 = new Attribute("key1", null);
        Attribute attribute5 = new Attribute("key1", "value2");

        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute5.hashCode());
    }

    /**
     * Test method clone().
     */
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals("key", clonedAttribute.getKey());
        assertEquals("value", clonedAttribute.getValue());
        assertEquals("key=value", clonedAttribute.html());
    }

    /**
     * Test method isDataAttribute().
     */
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    /**
     * Test method isDataAttribute(String key).
     */
    @Test
    public void testIsDataAttributeString() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(Attribute.isDataAttribute("key"));

        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    /**
     * Test method shouldCollapseAttribute().
     */
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();

        Attribute attribute1 = new Attribute("key1", "value1");
        assertFalse(attribute1.shouldCollapseAttribute(out));

        Attribute attribute2 = new Attribute("data-key1", null);
        assertTrue(attribute2.shouldCollapseAttribute(out));

        Attribute attribute3 = new Attribute("key1", "");
        assertTrue(attribute3.shouldCollapseAttribute(out));

        Attribute attribute4 = new Attribute("key1", "value1");
        assertTrue(attribute4.shouldCollapseAttribute(out));

        Attribute attribute5 = new Attribute("key1", "value1");
        Attribute attribute6 = new Attribute("key1", "value1");
        assertTrue(attribute5.shouldCollapseAttribute(out) && attribute6.shouldCollapseAttribute(out));
    }

    /**
     * Test method isBooleanAttribute().
     */
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    /**
     * Test method isBooleanAttribute(String key).
     */
    @Test
    public void testIsBooleanAttributeString() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }
}