package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
        assertEquals(attrWithParent.getParent(), attrWithParent.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        Attributes parent = new Attributes();
        Attribute attrWithParent = new Attribute("key", "value", parent);
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
        assertSame(parent, attrWithParent.getParent());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attributes parent = new Attributes();
        Attribute attrWithParent = new Attribute("key", "value", parent);
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
        assertSame(parent, attrWithParent.getParent());
    }

    @Test
    public void testHtml() throws Exception {
        String key = "key";
        String value = "value";
        Attribute attr = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, null);
        String html = StringUtil.releaseBuilder(sb);

        assertEquals("key=\"" + value + '\"', html);

        attr = new Attribute(key, null);
        sb.setLength(0);
        attr.html(sb, null);
        html = StringUtil.releaseBuilder(sb);
        assertEquals("key", html);

        attr = new Attribute(key, "");
        sb.setLength(0);
        attr.html(sb, null);
        html = StringUtil.releaseBuilder(sb);
        assertEquals("key", html);

        attr = new Attribute("", value);
        sb.setLength(0);
        attr.html(sb, null);
        html = StringUtil.releaseBuilder(sb);
        assertEquals("" + value, html);
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", "checked");
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("checked");
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("unchecked", null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("unchecked");
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("unspecified", null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("unspecified");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testCollapsibleAttribute() {
        Attribute attr = new Attribute("checked", "checked");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("unchecked", "");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("unchecked", "unchecked");
        assertFalse(attr.shouldCollapseAttribute(null));

        Document.OutputSettings out = new Document.OutputSettings();
        attr = new Attribute("checked", "checked");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr = new Attribute("checked", "");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr = new Attribute("checked", "unchecked");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr = new Attribute("unchecked", "");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr = new Attribute("unchecked", "checked");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testEqualsAndHashCode() {
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        Attribute attr1 = new Attribute(key, value1);
        Attribute attr2 = new Attribute(key, value1);
        Attribute attr3 = new Attribute(key, value2);
        Attribute attr4 = new Attribute("key", value1);
        Attribute attr5 = new Attribute("Key", value1);

        assertEquals(attr1, attr2);
        assertEquals(attr1.hashCode(), attr2.hashCode());

        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1.hashCode(), attr3.hashCode());

        assertNotEquals(attr1, attr4);
        assertNotEquals(attr1.hashCode(), attr4.hashCode());

        assertNotEquals(attr1, attr5);
        assertNotEquals(attr1.hashCode(), attr5.hashCode());
    }

    @Test
    public void testClone() {
        String key = "key";
        String value = "value";

        Attribute attr = new Attribute(key, value);
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}