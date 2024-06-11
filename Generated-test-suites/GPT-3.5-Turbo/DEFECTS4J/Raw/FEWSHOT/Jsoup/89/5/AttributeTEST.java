package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertNull(attr.parent);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldVal = attr.setValue("newValue");
        assertEquals("value", oldVal);
        assertEquals("newValue", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", attr.getKey());
        assertEquals("encodedValue", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", "value");
        assertEquals(true, attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr1 = new Attribute("key", "");
        Attribute attr2 = new Attribute("key", "key");
        assertEquals(true, attr1.shouldCollapseAttribute(out));
        assertEquals(true, attr2.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr1 = new Attribute("key1", "value");
        Attribute attr2 = new Attribute("key2", null);
        assertEquals(false, attr1.isBooleanAttribute());
        assertEquals(true, attr2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key", "newValue");
        Attribute attr4 = new Attribute("newKey", "value");
        assertEquals(true, attr1.equals(attr2));
        assertEquals(false, attr1.equals(attr3));
        assertEquals(false, attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = attr1.clone();
        assertEquals(attr1, attr2);
    }
}