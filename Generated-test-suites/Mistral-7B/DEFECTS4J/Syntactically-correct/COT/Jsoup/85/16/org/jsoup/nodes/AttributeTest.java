package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

/**
 * Tests for {@link Attribute} class.
 */
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
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        Attributes parent = new Attributes();
        attribute.setKey("newKey", parent);
        assertEquals("newKey", attribute.getKey());
        assertEquals(attribute, parent.get("newKey"));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        Attributes parent = new Attributes();
        attribute.setValue("newValue", parent);
        assertEquals("newValue", attribute.getValue());
        assertEquals(attribute, parent.get("key"));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        new Attribute("key", "value").html(sb, new Document("") {
            @Override
            public OutputStream outputStream() throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
            }
        }.outputSettings());
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

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        attribute.setKey("key");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setKey("booleanAttribute");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setKey("booleanAttribute");
        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute.setKey("booleanAttribute");
        attribute.setValue("booleanAttribute");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertFalse(attribute1.equals(attribute2));
        assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneWithCloneNotSupportedException() {
        new Attribute("key") {
            @Override
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {}
        }.clone();
    }
}