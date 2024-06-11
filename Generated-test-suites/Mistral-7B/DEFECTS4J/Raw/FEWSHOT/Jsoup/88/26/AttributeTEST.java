package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testConstructorFromString() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testConstructorFromStringAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("class", "foo", parent);
        assertEquals("class", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertSame(parent, attr.parent);
    }

    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("class", attr.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("class", "foo");
        attr.setKey("id");
        assertEquals("id", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("class", "foo");
        attr.setKey(null);
    }

    @Test
    public void testGetValue() {
        Attribute attr = new Attribute("class", "foo");
        assertEquals("foo", attr.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("class", "foo");
        attr.setValue("bar");
        assertEquals("bar", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("class", "foo");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("class", "foo");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(sb);
        assertEquals("class=\"foo\"", result);
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean result = Attribute.shouldCollapseAttribute("class", null, null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "foo", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class=foo", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class=\"foo\"", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class=\"foo\" foo", null);
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class=\"foo\" foo", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertFalse(result);
        result = Attribute.shouldCollapseAttribute("class", "class=\"foo\" foo", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
        assertTrue(result);
        result = Attribute.shouldCollapseAttribute("checked", null, null);
        assertTrue(result);
        result = Attribute.shouldCollapseAttribute("checked", "", null);
        assertTrue(result);
        result = Attribute.shouldCollapseAttribute("checked", "checked", null);
        assertTrue(result);
        result = Attribute.shouldCollapseAttribute("checked", "checked checked", null);
        assertTrue(result);
        result = Attribute.shouldCollapseAttribute("checked", "checked checked", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
        assertTrue(result);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("class"));
        assertFalse(Attribute.isBooleanAttribute(""));
        assertFalse(Attribute.isBooleanAttribute(null));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-foo"));
        assertFalse(Attribute.isDataAttribute("class"));
        assertFalse(Attribute.isDataAttribute(""));
        assertFalse(Attribute.isDataAttribute(null));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("id", "bar");
        Attribute attr4 = new Attribute("class", "foo");
        Attribute attr5 = attr1;
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr4));
        assertTrue(attr1.equals(attr5));
        assertFalse(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("class", "foo");
        Attribute attr2 = new Attribute("class", "foo");
        Attribute attr3 = new Attribute("id", "bar");
        Attribute attr4 = new Attribute("class", "foo");
        Attribute attr5 = attr1;
        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr4.hashCode());
        assertEquals(attr1.hashCode(), attr5.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("class", "foo");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.parent);
    }
}