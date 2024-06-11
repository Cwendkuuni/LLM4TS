package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AttributeTest {
    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }

    @Test
    public void testConstructor() {
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.get(attribute.getKey()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get(attribute.getKey()));
    }

    @Test
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value", parent);
        String oldValue = attribute.setValue(null);
        assertNull(attribute.getValue());
        assertEquals("value", oldValue);
        assertNull(parent.get(attribute.getKey()));
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testHtmlShouldCollapseAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();
        attribute.setValue("key");
        assertTrue(attribute.shouldCollapseAttribute(settings));
    }

    @Test
    public void testHtmlShouldNotCollapseAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();
        attribute.setValue("value");
        assertFalse(attribute.shouldCollapseAttribute(settings));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value", parent) {
            @Override
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
                throw new IOException();
            }
        };
        attribute.html();
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value", parent);
        Attribute attribute2 = new Attribute("key", "value", parent);
        Attribute attribute3 = new Attribute("key", null, parent);
        Attribute attribute4 = new Attribute("newKey", "value", parent);
        Attribute attribute5 = new Attribute("key", "newValue", parent);
        Attribute attribute6 = new Attribute("newKey", "newValue", parent);

        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));
        assertTrue(attribute1.equals(attribute1));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
        assertFalse(attribute1.equals(attribute6));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value", parent);
        Attribute attribute2 = new Attribute("key", "value", parent);

        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloned = attribute.clone();
        assertEquals(attribute, cloned);
        assertFalse(attribute == cloned);
    }
}