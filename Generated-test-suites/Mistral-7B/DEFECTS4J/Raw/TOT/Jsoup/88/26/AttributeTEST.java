package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attributeWithParent.getParent());
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setKey("newKey");
        assertEquals("newKey", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertEquals(newAttributes("newKey", "value"), attributeWithParent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeyNull() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        new Attribute("", "value");
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setValue("newValue");
        assertEquals("newValue", attributeWithParent.getValue());
        assertEquals(newAttributes("key", "newValue"), attributeWithParent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueNull() {
        new Attribute("key", null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);

        Attribute booleanAttribute = new Attribute("checked", "");
        booleanAttribute.html(sb, new Document("").outputSettings());
        html = StringUtil.releaseBuilder(sb);
        assertEquals("checked", html);
    }

    @Test
    public void testAttributeToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());

        Attribute booleanAttribute = new Attribute("checked", "");
        assertEquals("checked", booleanAttribute.toString());
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());

        Attribute nonDataAttribute = new Attribute("key", "value");
        assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", "");
        assertTrue(booleanAttribute.shouldCollapseAttribute(out));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(out));
    }

    private Attributes newAttributes(String... entries) {
        Attributes attributes = new Attributes();
        for (String entry : entries) {
            String[] pair = entry.split("=");
            attributes.put(new Attribute(pair[0], pair[1]), null);
        }
        return attributes;
    }
}