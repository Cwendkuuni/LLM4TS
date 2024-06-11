package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        StringBuilder sb2 = StringUtil.borrowBuilder();
        attrWithParent.html(sb2, new Document("").outputSettings());
        String expected2 = "key=\"value\"";
        assertEquals(expected2, StringUtil.releaseBuilder(sb2));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals(attr.html(), attr.toString());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals(attrWithParent.html(), attrWithParent.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
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
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "value");
        assertTrue(attr.shouldCollapseAttribute(out));

        Attribute booleanAttr = new Attribute("checked", "");
        assertTrue(booleanAttr.shouldCollapseAttribute(out));

        Attribute attrWithValue = new Attribute("key", "value-different-than-key");
        assertFalse(attrWithValue.shouldCollapseAttribute(out));

        Document.OutputSettings htmlSyntax = new Document.OutputSettings(Document.OutputSettings.Syntax.html);
        Attribute booleanAttrHtmlSyntax = new Attribute("checked", "");
        assertTrue(booleanAttrHtmlSyntax.shouldCollapseAttribute(htmlSyntax));

        Attribute booleanAttrHtmlSyntaxWithValue = new Attribute("checked", "checked");
        assertTrue(booleanAttrHtmlSyntaxWithValue.shouldCollapseAttribute(htmlSyntax));

        Document.OutputSettings xmlSyntax = new Document.OutputSettings(Document.OutputSettings.Syntax.xml);
        Attribute booleanAttrXmlSyntax = new Attribute("checked", "");
        assertFalse(booleanAttrXmlSyntax.shouldCollapseAttribute(xmlSyntax));

        Attribute booleanAttrXmlSyntaxWithValue = new Attribute("checked", "checked");
        assertFalse(booleanAttrXmlSyntaxWithValue.shouldCollapseAttribute(xmlSyntax));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertTrue(booleanAttr.isBooleanAttribute());

        Attribute booleanAttrWithValue = new Attribute("disabled", "disabled");
        assertTrue(booleanAttrWithValue.isBooleanAttribute());

        Attribute attrWithBooleanPrefix = new Attribute("data-boolean", "value");
        assertFalse(attrWithBooleanPrefix.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        assertEquals(hashCode1, hashCode2); // same key and value should have same hashcode
        assertNotEquals(hashCode1, hashCode3); // different key or value should have different hashcode
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}