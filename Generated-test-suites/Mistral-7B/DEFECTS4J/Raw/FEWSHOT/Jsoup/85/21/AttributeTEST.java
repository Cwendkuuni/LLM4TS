package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testCreateFromUnencodedKeyValue() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testCreateFromUnencodedKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("class", "foo", parent);
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertSame(parent, attr.getParent());
        assertEquals(attr, parent.get("class"));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("class", "foo");
        attr.setKey("id");
        assertEquals("id", attr.getKey());
        assertNull(attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("class", "foo");
        attr.setValue("bar");
        assertEquals("class", attr.getKey());
        assertEquals("bar", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testSetValueParentIsUpdated() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("class", "foo", parent);
        attr.setValue("bar");
        assertEquals("class", attr.getKey());
        assertEquals("bar", attr.getValue());
        assertEquals(attr, parent.get("class"));
        assertEquals("bar", parent.getValue("class"));
    }

    @Test
    public void testHtmlNormal() throws IOException {
        Attribute attr = new Attribute("class", "foo");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("class=\"foo\"", sb.toString());
    }

    @Test
    public void testHtmlCollapsibleBoolean() throws IOException {
        Attribute attr = new Attribute("checked", null);
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertTrue(sb.toString().equals("checked") || sb.toString().isEmpty());
    }

    @Test
    public void testHtmlCollapsibleBooleanWithParent() throws IOException {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("checked", null, parent);
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertTrue(sb.toString().equals("checked") || sb.toString().isEmpty());
        assertEquals(attr, parent.get("checked"));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-foo"));
        assertFalse(Attribute.isDataAttribute("class"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("class", "bar");
        Attribute attr4 = attr1;
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(null));
        assertFalse(attr1.equals("bar"));
        assertTrue(attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("class", "bar");
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("class", "foo");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
        assertSame(attr.getParent(), clonedAttr.getParent());
        attr.setValue("bar");
        assertNotSame(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), null);
        assertEquals(clonedAttr.getValue(), "foo");
    }
}