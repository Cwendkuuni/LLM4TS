package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

/**
 * Test class for Attribute.
 */
public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attribute.getParent());
        assertNotNull(attributeWithParent.getParent());
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");

        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        attribute.setValue(newValue);

        assertEquals("key", attribute.getKey());
        assertEquals(newValue, attribute.getValue());
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder expectedHtml = StringUtil.borrowBuilder();
        Attribute.html("key", "value", expectedHtml, new Document.OutputSettings());
        String actualHtml = attribute.html();

        assertEquals(StringUtil.releaseBuilder(expectedHtml), actualHtml);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        throw new IOException();

        attribute.html(sb, new Document.OutputSettings());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data:key", "value");
        assertTrue(dataAttribute.isDataAttribute());
        Attribute nonDataAttribute = new Attribute("key", "value");
        assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        assertTrue(new Attribute("key", "").shouldCollapseAttribute(outputSettings));
        assertTrue(new Attribute("key", "key").shouldCollapseAttribute(outputSettings));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(outputSettings));
        assertFalse(new Attribute("key", "value1").shouldCollapseAttribute(outputSettings));
        assertFalse(new Attribute("key", "value1").shouldCollapseAttribute(outputSettings, Attributes.Syntax.xml));
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotNull(attribute.getParent());
        assertNull(clonedAttribute.getParent());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneMethodException() {
        Attribute attribute = new Attribute("key", "value");
        throw new CloneNotSupportedException();
    }
}