package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, attrParent.get("key"));
    }

    @Test
    public void testAttributeKeySet() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        new Attribute("", "value");
    }

    @Test
    public void testAttributeValueSet() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "key");
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));

        attr.setKey("newKey");
        assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        attr.setKey("checked");
        assertTrue(attr.isBooleanAttribute());
    }
}