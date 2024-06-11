package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeConstructionWithStringKeyAndStringValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithStringKeyStringValueAndAttributesParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethodWithEmptyOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document.OutputSettings());
        String result = StringUtil.releaseBuilder(sb);
        assertTrue(result.contains("key=\"value\""));
    }

    @Test
    public void testHtmlMethodWithNotCollapsibleOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        attribute.html(sb, out);
        String result = StringUtil.releaseBuilder(sb);
        assertTrue(result.contains("key=\"value\""));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndEmptyValue() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndValueSameAsKey() {
        Attribute attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndValueDifferentFromKey() {
        Attribute attribute = new Attribute("checked", "notchecked");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithNonBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHtmlMethodWithNullOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithIOException() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        try {
            attribute.html(sb, new Document.OutputSettings());
            throw new AssertionError("IOException not thrown");
        } catch (IOException e) {
        }
    }

    @Test
    public void testCreateFromEncodedWithUnencodedKeyAndEncodedValue() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanAttributeKey() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testIsBooleanAttributeWithNonBooleanAttributeKey() {
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEqualsWithSameObject() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1;
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testEqualsWithDifferentObjectsWithSameKeyAndValue() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testEqualsWithDifferentObjectsWithDifferentKeys() {
        Attribute attribute1 = new Attribute("key1", "value");
        Attribute attribute2 = new Attribute("key2", "value");
        assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testEqualsWithDifferentObjectsWithDifferentValues() {
        Attribute attribute1 = new Attribute("key", "value1");
        Attribute attribute2 = new Attribute("key", "value2");
        assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1;
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
    }
}