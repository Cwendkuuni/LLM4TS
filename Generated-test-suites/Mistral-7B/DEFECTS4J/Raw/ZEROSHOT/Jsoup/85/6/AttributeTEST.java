package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, attributes.get("key"));
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.getValue();
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertNotEquals(oldValue, attribute.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(expected));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
        attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        attribute = new Attribute("key", "value", null);
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key", "value");
        Attribute attribute4 = new Attribute("key", "value");
        Attribute attribute5 = attribute;

        assertTrue(attribute.equals(attribute5));
        assertTrue(attribute5.equals(attribute));

        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals(attribute1));
        assertFalse(attribute.equals(attribute2));
        assertFalse(attribute.equals(attribute3));
        assertFalse(attribute.equals(attribute4));
        assertFalse(attribute.equals("value"));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key", "value");
        Attribute attribute4 = new Attribute("key", "value");
        Attribute attribute5 = attribute;

        int hashCode = attribute.hashCode();
        assertEquals(hashCode, attribute.hashCode());
        assertEquals(hashCode, attribute5.hashCode());
        assertNotEquals(hashCode, attribute1.hashCode());
        assertNotEquals(hashCode, attribute2.hashCode());
        assertNotEquals(hashCode, attribute3.hashCode());
        assertNotEquals(hashCode, attribute4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attributeClone = attribute.clone();
        assertEquals(attribute, attributeClone);
        assertNotSame(attribute, attributeClone);
        assertEquals(attribute.getKey(), attributeClone.getKey());
        assertEquals(attribute.getValue(), attributeClone.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean booleanResult = attribute.isBooleanAttribute();
        assertFalse(booleanResult);
        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapOperations() {
        Map<String, Attribute> map = new Attributes();
        map.put("key", attribute);
        map.remove("key");
    }
}