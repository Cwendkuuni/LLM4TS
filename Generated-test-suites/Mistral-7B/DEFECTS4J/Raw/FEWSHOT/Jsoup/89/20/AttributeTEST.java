package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrParent = new Attributes();
        attr.setParent(attrParent);
        attrParent.put(attr);
        assertEquals(attr, attrParent.get("key"));
        assertEquals("value", attrParent.get("key").getValue());

        attr.setKey("newKey");
        assertEquals("newKey", attrParent.get("newKey").getKey());
        assertEquals("value", attrParent.get("newKey").getValue());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attributes attrs = new Attributes();
        Attribute attr = new Attribute("key", "value", attrs);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attrs, attr.getParent());
        attrs.put(attr);
        assertEquals(attr, attrs.get("key"));
        assertEquals("value", attrs.get("key").getValue());

        attr.setKey("newKey");
        assertEquals("newKey", attrs.get("newKey").getKey());
        assertEquals("value", attrs.get("newKey").getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb);
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Document doc = new Document();
        attr.html(sb, doc.outputSettings());
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute(null));

        Attribute attrHtml5 = new Attribute("checked", "");
        assertTrue(attrHtml5.shouldCollapseAttribute(null, null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

        Attribute attrNoHtml = new Attribute("checked", "checked");
        assertFalse(attrNoHtml.shouldCollapseAttribute(null));

        Attribute attrFalse = new Attribute("notchecked", null);
        assertFalse(attrFalse.shouldCollapseAttribute(null));

        Attribute attrNotBoolean = new Attribute("id", "value");
        assertFalse(attrNotBoolean.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertFalse(attr.isBooleanAttribute());

        Attribute attrBoolean = new Attribute("checked", "");
        assertTrue(attrBoolean.isBooleanAttribute());

        Attribute attrNotBoolean = new Attribute("id", "value");
        assertFalse(attrNotBoolean.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr1.hashCode();
        int hashCode3 = attr2.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}