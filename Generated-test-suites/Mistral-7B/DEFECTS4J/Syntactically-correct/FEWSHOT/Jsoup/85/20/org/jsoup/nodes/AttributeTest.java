package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attr = new Attribute("class", "foo", parentAttributes);
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertEquals(1, parentAttributes.size());
        assertEquals(attr, parentAttributes.values().iterator().next());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("class", "foo");
        attr.setKey("id");
        assertEquals("id", attr.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("class", "foo");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("class", "foo");
        attr.setValue("bar");
        assertEquals("bar", attr.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("class", "foo");
        attr.setValue(null);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("class", null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("class", "data-foo");
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("foo", "value");
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("checked", "checked");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("id", "id");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("class", "class");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("foo", "bar");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("data-foo", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("data-foo", "data-foo");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("data-foo", "value");
        assertFalse(attr.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldCollapseAttributeWithOutputSettings() {
        Document.OutputSettings outSettings = new Document.OutputSettings();
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("checked", "checked");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("id", "id");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("class", "class");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("foo", "bar");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "data-foo");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "value");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        outSettings.syntax(Document.OutputSettings.Syntax.xml);
        attr = new Attribute("checked", "");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("checked", "checked");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("class", "class");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("foo", "bar");
        assertFalse(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "data-foo");
        assertTrue(attr.shouldCollapseAttribute(outSettings));

        attr = new Attribute("data-foo", "value");
        assertFalse(attr.shouldCollapseAttribute(outSettings));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("id", "foo");
        Attribute attr4 = new Attribute("class", "bar");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("id", "foo");
        Attribute attr4 = new Attribute("class", "bar");

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("class", "foo");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNotSame(attr, clonedAttr);
    }
}