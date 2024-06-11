package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PutAttribute;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * AttributeTest.java
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parentAttributes.get("key"));
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
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeySetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        assertEquals("value", attribute.getValue());
        attribute.setValue(newValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlEncoding() throws IOException {
        Attribute attribute = new Attribute("test", "test < value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("test=\"test &lt; value\"", sb.toString());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("test", "test=\"value\"");
        assertEquals("test", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("test", "value");
        assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-test", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document document = new Document();
        StringBuilder sb = StringUtil.borrowBuilder();
        Attribute attribute = new Attribute("test", "");
        assertTrue(attribute.shouldCollapseAttribute(document.outputSettings()));
        attribute.setValue("test");
        assertFalse(attribute.shouldCollapseAttribute(document.outputSettings()));

        attribute = new Attribute("booleanTest", "");
        assertTrue(attribute.shouldCollapseAttribute(document.outputSettings()));
        attribute.setValue("booleanTest");
        assertFalse(attribute.shouldCollapseAttribute(document.outputSettings()));

        attribute = new Attribute("booleanTest", "booleanTest");
        assertTrue(attribute.shouldCollapseAttribute(document.outputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanTest", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("test", null);
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("booleanTest", "false");
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("test", "false");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test1", "value1");
        Attribute attribute3 = new Attribute("test2", "value2");
        Attribute attribute4 = new Attribute("test1", null);

        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
        assertNotEquals(attribute1, attribute4);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("test1", "value1");
        Attribute attribute2 = new Attribute("test1", "value1");
        Attribute attribute3 = new Attribute("test2", "value2");
        Attribute attribute4 = new Attribute("test1", null);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        int hashCode4 = attribute4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("test", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attribute attribute = new Attribute("test", "value");
        attribute.clone();
    }

    @Test
    public void testSerialization() throws IOException {
        Attribute attribute = new Attribute("test", "value");
        Element element = new Element("test");
        element.attribute(attribute.getKey(), attribute.getValue());

        StringBuilder sb = StringUtil.borrowBuilder();
        element.html(sb, new Document("").outputSettings());
        assertEquals("test=\"value\"", sb.toString());
    }
}