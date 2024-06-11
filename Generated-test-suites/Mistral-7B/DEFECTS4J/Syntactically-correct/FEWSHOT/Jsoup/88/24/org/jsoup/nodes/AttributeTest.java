package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testCreation() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("key", attrParent.getKey());
        assertEquals("value", attrParent.getValue());
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("value", attr.getValue());
        assertEquals("newValue", attr.setValue("newerValue"));
        assertEquals("newerValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(new Attribute("data-key", null).isDataAttribute());
        assertFalse(new Attribute("key", null).isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(new Attribute("checked", null).isBooleanAttribute());
        assertFalse(new Attribute("key", null).isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(new Attribute("checked", null).shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        throw new SerializationException(new IOException());
    }
}