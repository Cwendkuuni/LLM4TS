package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, parent.get(attr.getKey()));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertNotEquals("key", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertNotEquals("value", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attr.toString());
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

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr.setKey("key");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr.setKey("boolean");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setKey("data:boolean");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBooleanAttributeDeprecated() {
        Attribute attr = new Attribute("boolean", null);
        attr.isBooleanAttribute();
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("boolean", null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
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

        int hashcode1 = attr1.hashCode();
        int hashcode2 = attr2.hashCode();
        int hashcode3 = attr3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }
}