package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import org.junit.Test;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(1, parent.size());
        assertEquals(attr, parent.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyCreation() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueCreation() {
        new Attribute("key", null);
    }

    @Test
    public void testKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testKeySetterWithSameValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("key");
        assertEquals("key", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test
    public void testValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", result);
    }

    @Test
    public void testToStringMethod() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attr = Attribute.createFromEncoded("key", "%6B%65%78%6F%6F%6C%6C%69%64%3D%22%6F%6E%6C%6F%62%65%61%75%65%6E%22");
        assertEquals("key", attr.getKey());
        assertEquals("fooBar", attr.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());
        attr.setKey("data-key");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() throws Exception {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "");
        assertFalse(attr.shouldCollapseAttribute(out));
        attr.setKey("booleanAttribute");
        assertTrue(attr.shouldCollapseAttribute(out));
        attr.setValue("true");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttributeDeprecatedMethod() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());
        attr.setKey("checked");
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(Attribute.isBooleanAttribute(attr.getKey()));
        attr.setKey("checked");
        assertTrue(Attribute.isBooleanAttribute(attr.getKey()));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}