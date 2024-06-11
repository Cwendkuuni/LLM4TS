package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attr2 = new Attribute("key", "value", new Attributes(new Object() {}));
        assertEquals("key", attr2.getKey());
        assertEquals("value", attr2.getValue());
        assertSame(attr2.getParent(), new Attributes(new Object() {}));
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
        StringBuilder builder = StringUtil.borrowBuilder();
        attr.html(builder, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(builder));
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute("", null, new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "", new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings(Document.OutputSettings.Syntax.xml)));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedNullKey() {
        Attribute.createFromEncoded(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedNullValue() {
        Attribute.createFromEncoded("key", null);
    }
}