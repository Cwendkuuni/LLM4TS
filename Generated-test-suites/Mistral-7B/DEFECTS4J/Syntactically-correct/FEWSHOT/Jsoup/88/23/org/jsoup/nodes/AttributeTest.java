package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNull(attrWithParent.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValue() {
        new Attribute("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertTrue(html.contains("key=\"value\""));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, new Document("").outputSettings().syntax(Document.OutputSettings.Syntax.xml));
        String xml = StringUtil.releaseBuilder(sb2);
        assertTrue(xml.contains("key=\"value\""));
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

        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }

    @Test
    public void testBooleanAttributes() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("required"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testBooleanAttributesValue() {
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.shouldCollapseAttribute());

        attr.setValue("checked");
        assertTrue(attr.isDataAttribute());
        assertFalse(attr.shouldCollapseAttribute());

        attr.setValue("false");
        assertFalse(attr.isDataAttribute());
        assertFalse(attr.shouldCollapseAttribute());

        attr.setValue("");
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.shouldCollapseAttribute());
    }
}