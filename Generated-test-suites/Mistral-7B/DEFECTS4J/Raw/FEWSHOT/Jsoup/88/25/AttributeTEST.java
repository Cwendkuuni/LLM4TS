package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testIsBooleanAttributeDeprecated() {
        Attribute attribute = new Attribute("checked", null);
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", "true");
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("formnovalidate", null);
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("formnovalidate", "true");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertFalse(Attribute.isBooleanAttribute(attribute.getKey()));

        attribute = new Attribute("checked", "true");
        assertTrue(Attribute.isBooleanAttribute(attribute.getKey()));

        attribute = new Attribute("formnovalidate", null);
        assertFalse(Attribute.isBooleanAttribute(attribute.getKey()));

        attribute = new Attribute("formnovalidate", "true");
        assertTrue(Attribute.isBooleanAttribute(attribute.getKey()));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("class", "foo bar baz");
        assertEquals("class", attribute.getKey());
        assertEquals("foo bar baz", attribute.getValue());

        attribute = Attribute.createFromEncoded("class", "foo");
        assertEquals("class", attribute.getKey());
        assertNull(attribute.getValue());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("checked", "false");
        assertFalse(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("checked", "true");
        assertTrue(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("checked", "");
        assertTrue(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("disabled", null);
        assertFalse(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("disabled", "");
        assertFalse(attribute.shouldCollapseAttribute(null));

        attribute = new Attribute("disabled", "disabled");
        assertTrue(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldCollapseAttributeWithOutputSettings() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("checked", "false");
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("checked", "true");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("checked", "");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("disabled", null);
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("disabled", "");
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));

        attribute = new Attribute("disabled", "disabled");
        assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("foo", "bar");
        attribute.setKey("baz");
        assertEquals("baz", attribute.getKey());

        attribute = new Attribute("foo", null);
        attribute.setKey("baz");
        assertEquals("baz", attribute.getKey());

        attribute = new Attribute("foo", "bar");
        attribute.setKey("foo");
        assertEquals("foo", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("foo", "bar");
        attribute.setValue("baz");
        assertEquals("baz", attribute.getValue());

        attribute = new Attribute("foo", null);
        attribute.setValue("baz");
        assertEquals("baz", attribute.getValue());

        attribute = new Attribute("foo", "bar");
        attribute.setValue("bar");
        assertEquals("bar", attribute.getValue());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("foo", "bar");
        Attribute cloned = attribute.clone();
        assertEquals(attribute, cloned);
        assertSame(attribute.getKey(), cloned.getKey());
        assertSame(attribute.getValue(), cloned.getValue());
    }
}