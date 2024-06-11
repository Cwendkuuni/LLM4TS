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

    @Test
    public void testAttributeSetters() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        attrWithParent.setKey("newKey");
        assertEquals("newKey", attrWithParent.getKey());
        attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
        assertEquals(attrWithParent, attrWithParent.getParent().get(attrWithParent.getKey()));
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));

        Attribute booleanAttr = new Attribute("checked", "");
        boolean attrIsCollapsed = booleanAttr.shouldCollapseAttribute(new Document.OutputSettings());
        assertTrue(attrIsCollapsed);
        assertEquals("checked", booleanAttr.getKey());
        assertEquals(null, booleanAttr.getValue());

        StringBuilder sb2 = StringUtil.borrowBuilder();
        booleanAttr.html(sb2, new Document.OutputSettings());
        assertEquals("checked", StringUtil.releaseBuilder(sb2));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedNullKey() {
        Attribute.createFromEncoded(null, "key=\"value\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedInvalidKey() {
        Attribute.createFromEncoded("invalid", "key=\"value\"");
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(Attribute.isBooleanAttribute("key"));
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());

        Attribute nonDataAttr = new Attribute("key", "value");
        assertFalse(nonDataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute booleanAttr = new Attribute("checked", "");
        assertTrue(booleanAttr.shouldCollapseAttribute(settings));

        Attribute nonBooleanAttr = new Attribute("key", "value");
        assertFalse(nonBooleanAttr.shouldCollapseAttribute(settings));

        settings.syntax(Document.OutputSettings.Syntax.xml);
        booleanAttr = new Attribute("checked", "");
        assertFalse(booleanAttr.shouldCollapseAttribute(settings));
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
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }
}