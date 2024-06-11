package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.getParent());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(parent, attribute.getParent());
    }

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNotSame(attribute.getKey(), "key");
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        assertEquals("value", attribute.getValue());
        String oldValue = attribute.setValue(newValue);
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
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
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setValue("value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        attribute.setValue("key");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithKeyOnly() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test(expected = DeprecationException.class)
    public void testIsBooleanAttributeDeprecated() {
        Attribute attribute = new Attribute("key", "value");
        attribute.isBooleanAttribute();
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", null);
        Attribute attribute5 = new Attribute("key1", "value1");
        Attribute attribute6 = attribute3;

        assertTrue(attribute1.equals(attribute3));
        assertTrue(attribute3.equals(attribute6));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals(new Object()));
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
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.getParent());
    }

    @Test
    public void testMapEntryMethods() {
        Attribute attribute = new Attribute("key", "value");
        Map<String, Attribute> map = new HashMap<>();
        map.put(attribute.getKey(), attribute);

        assertEquals(attribute.getKey(), map.keySet().iterator().next());
        assertEquals(attribute, map.values().iterator().next());

        assertEquals(attribute.getKey(), map.get("key").getKey());
        assertEquals(attribute, map.get("key"));

        Attribute modifiedAttribute = new Attribute("newKey", null);
        map.put(modifiedAttribute.getKey(), modifiedAttribute);

        assertEquals("newKey", map.get("newKey").getKey());
        assertNull(map.get("newKey").getValue());
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(Attribute.isBooleanAttribute("checked"));

        assertEquals(Arrays.asList("allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch"),
                Arrays.stream(booleanAttributes).boxed().collect(Collectors.toList()));
    }
}