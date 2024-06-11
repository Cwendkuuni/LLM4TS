package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testConstructorUnencodedKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testConstructorUnencodedKeyAndValueWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parentAttributes, attribute.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("key", attribute.getKey());
        assertEquals("newValue", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute1 = new Attribute("key1", "");
        assertFalse(attribute1.shouldCollapseAttribute(outputSettings));
        Attribute attribute2 = new Attribute("key2", "key2");
        assertFalse(attribute2.shouldCollapseAttribute(outputSettings));
        Attribute attribute3 = new Attribute("booleanAttribute", "");
        assertTrue(attribute3.shouldCollapseAttribute(outputSettings));
        Attribute attribute4 = new Attribute("booleanAttribute", "true");
        assertTrue(attribute4.shouldCollapseAttribute(outputSettings));
        Attribute attribute5 = new Attribute("booleanAttribute", "false");
        assertTrue(attribute5.shouldCollapseAttribute(outputSettings));
        Attribute attribute6 = new Attribute("booleanAttribute", "otherValue");
        assertFalse(attribute6.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithFalseValue() {
        Attribute booleanAttribute = new Attribute("checked", "false");
        assertFalse(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithNullValue() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", null);
        Attribute attribute5 = new Attribute("key1", "value1");
        assertTrue(attribute1.equals(attribute3));
        assertTrue(attribute1.equals(attribute5));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals("otherValue"));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", null);
        Attribute attribute5 = new Attribute("key1", "value1");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();
        int hashCode5 = attribute5.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, 42);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals("key", clonedAttribute.getKey());
        assertEquals("value", clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }
}