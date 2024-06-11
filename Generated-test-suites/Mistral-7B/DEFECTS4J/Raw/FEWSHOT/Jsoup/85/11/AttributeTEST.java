package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateNewAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testCreateNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(parent, attr.getParent());
        assertEquals(1, parent.size());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("data-checked", "true");
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(Attribute.isBooleanAttribute(attr.getKey()));

        attr = new Attribute("checked", null);
        assertTrue(Attribute.isBooleanAttribute(attr.getKey()));

        attr = new Attribute("data-checked", "true");
        assertTrue(Attribute.isBooleanAttribute(attr.getKey()));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Document.OutputSettings.Syntax.html);

        Attribute attr1 = new Attribute("key1", "value1");
        assertFalse(attr1.shouldCollapseAttribute(out));

        Attribute attr2 = new Attribute("key2", "");
        assertTrue(attr2.shouldCollapseAttribute(out));

        Attribute attr3 = new Attribute("key3", "key3");
        assertTrue(attr3.shouldCollapseAttribute(out));

        Attribute attr4 = new Attribute("booleanAttribute", "");
        assertTrue(attr4.shouldCollapseAttribute(out));

        Attribute attr5 = new Attribute("booleanAttribute", "true");
        assertTrue(attr5.shouldCollapseAttribute(out));

        Attribute attr6 = new Attribute("booleanAttribute", "false");
        assertFalse(attr6.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertEquals(attr1, attr3);
        assertNotEquals(attr1, attr2);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertEquals(attr.getParent(), clonedAttr.getParent());
    }
}