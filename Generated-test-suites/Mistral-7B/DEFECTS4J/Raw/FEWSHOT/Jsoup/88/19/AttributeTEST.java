package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        parent.put("key", attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get("key"));
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
        attribute.setValue(newValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);
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
    public void testShouldCollapseAttributeFalse() throws IOException {
        Document document = new Document();
        Element element = new Element("div");
        element.attributes().put("key", new Attribute("key", "value"));
        StringBuilder sb = StringUtil.borrowBuilder();
        element.html(sb, document.outputSettings());
        String html = StringUtil.releaseBuilder(sb);

        assertFalse(new Attribute("key", null).shouldCollapseAttribute(document.outputSettings()));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(document.outputSettings()));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(document.outputSettings()));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(document.outputSettings()));

        assertTrue(html.contains("key=\"value\""));
    }

    @Test
    public void testShouldCollapseAttributeTrue() throws IOException {
        Document document = new Document();
        Element element = new Element("div");
        element.attributes().put("key", new Attribute("key", ""));
        StringBuilder sb = StringUtil.borrowBuilder();
        element.html(sb, document.outputSettings());
        String html = StringUtil.releaseBuilder(sb);

        assertTrue(new Attribute("key", "").shouldCollapseAttribute(document.outputSettings()));
        assertTrue(new Attribute("key", "").shouldCollapseAttribute(document.outputSettings()));
        assertTrue(new Attribute("key", "").shouldCollapseAttribute(document.outputSettings()));
        assertTrue(new Attribute("key", "").shouldCollapseAttribute(document.outputSettings()));

        assertTrue(html.contains("key"));
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test(expected = AssertionError.class)
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attribute.isBooleanAttribute(); // this method is not supposed to be called directly
    }

    @Test
    public void testIsBooleanAttributeStatic() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashcode1 = attr1.hashCode();
        int hashcode2 = attr2.hashCode();
        int hashcode3 = attr3.hashCode();
        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute original = new Attribute("key", "value");
        Attribute clone = original.clone();
        assertEquals(original.getKey(), clone.getKey());
        assertEquals(original.getValue(), clone.getValue());
    }
}