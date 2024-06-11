package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
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
        assertEquals(parent, attribute.getParent());
        assertEquals(attribute, parent.get("key"));
        assertEquals(attribute, parent.values()[0]);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertNotEquals("key", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertNotEquals(newValue, attribute.getOldValue());
        assertEquals("value", attribute.getOldValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", result);
    }

    @Test
    public void testHtmlWithEmptyValue() throws IOException {
        Attribute attribute = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"\"", result);
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
        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setKey("key");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setKey("data-key");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setValue(null);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setValue("value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute.setKey("checked");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        attribute.setKey("checked");
        assertTrue(attribute.isBooleanAttribute());

        // Deprecated method
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(attribute.isBooleanAttribute(attribute.getKey()));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.getParent(), clonedAttribute.getParent());
    }
}