package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for org.jsoup.nodes.Attribute
 */
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

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        new Attribute("key", "value").setKey(null);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        new Attribute("key", "value").setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithEmptyValue() throws IOException {
        Attribute attribute = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithBooleanAttribute() throws IOException {
        Attribute attribute = new Attribute("checked", null);
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("checked", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithSameKeyAndValue() throws IOException {
        Attribute attribute = new Attribute("key", "key");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertFalse(StringUtil.releaseBuilder(sb).contains("=\""));
    }

    @Test
    public void testHtmlWithNullParent() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.parent = null;
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
        attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeWithDefaultSettings() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntaxAndSameKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntaxAndEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntaxAndBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", null);

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute4));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Attribute attribute4 = new Attribute("key1", null);

        int hashcode1 = attribute1.hashCode();
        int hashcode2 = attribute2.hashCode();
        int hashcode3 = attribute3.hashCode();
        int hashcode4 = attribute4.hashCode();

        assertTrue(hashcode1 == hashcode3);
        assertNotSame(hashcode1, hashcode2);
        assertNotSame(hashcode1, hashcode4);
    }

    @Test
    public void testClone() {
        Attribute original = new Attribute("key", "value");
        Attribute cloned = original.clone();
        assertEquals("key", cloned.getKey());
        assertEquals("value", cloned.getValue());
        assertNotSame(original, cloned);
    }
}