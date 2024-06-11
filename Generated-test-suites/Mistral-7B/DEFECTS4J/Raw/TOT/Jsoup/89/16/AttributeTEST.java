package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.TextNode;
import org.jsoup.serializer.OutputSettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("class", "test");
        assertEquals("class", attr.getKey());
        assertEquals("test", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrParent = new Attributes();
        attr.setParent(attrParent);
        attrParent.put(attr);
        assertEquals(1, attrParent.size());
        assertTrue(attrParent.containsKey("class"));
        assertEquals(attr, attrParent.get("class"));

        assertEquals("class=\"test\"", attr.html());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes attrParent = new Attributes();
        Attribute attr = new Attribute("class", "test", attrParent);
        assertEquals("class", attr.getKey());
        assertEquals("test", attr.getValue());
        assertSame(attrParent, attr.getParent());

        attr.setValue("newValue");
        assertEquals("class=\"newValue\"", attr.html());
        assertTrue(attrParent.containsKey("class"));
        assertSame(attr, attrParent.get("class"));
    }

    @Test
    public void testBooleanAttribute() {
        Attribute attr = new Attribute("checked", "");
        assertFalse(attr.isBooleanAttribute());
        attr.setValue("checked");
        assertTrue(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("autofocus", null);
        assertTrue(booleanAttr.isBooleanAttribute());
    }

    @Test
    public void testCollapsibleBooleanAttribute() throws Exception {
        OutputSettings out = new OutputSettings();
        Attribute attr = new Attribute("autofocus", "");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setValue("autofocus");
        assertTrue(attr.shouldCollapseAttribute(out));

        Document doc = new Document();
        Elements ele = doc.createElement("input");
        ele.attr("autofocus", "");
        assertEquals("autofocus", ele.attr("autofocus"));

        ele.attr("autofocus", "autofocus");
        assertNull(ele.attr("autofocus"));

        StringBuilder sb = StringUtil.borrowBuilder();
        ele.html(sb, out);
        assertEquals("input autofocus", sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        Attribute attr = new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        Attribute attr = new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Attribute attr = new Attribute("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValue() {
        Attribute attr = new Attribute("key", "");
        attr.setValue("");
        assertEquals("", attr.getValue());
        assertFalse(attr.shouldCollapseAttribute(null));
    }

    @Test(expected = SerializationException.class)
    public void testHtml() throws Exception {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, null);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithNullKey() throws Exception {
        Attribute attr = new Attribute(null, "value");
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithNullValue() throws Exception {
        Attribute attr = new Attribute("key", null);
    }
}