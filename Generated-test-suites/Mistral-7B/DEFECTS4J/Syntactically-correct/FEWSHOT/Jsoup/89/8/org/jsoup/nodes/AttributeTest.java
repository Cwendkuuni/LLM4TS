package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

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
        assertEquals(attr, parent.get("key"));
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
        String expected = String.format("key=\"value\"", "");
        assertEquals(expected, StringUtil.releaseBuilder(sb));
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
        Attribute dataAttr = new Attribute(Attributes.dataPrefix + "key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setKey("key");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr.setValue("");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr.setValue("value");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setKey("booleanAttribute");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());
        attr.setKey("booleanAttribute");
        assertFalse(attr.isBooleanAttribute());
        attr.setKey("checked");
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);
        Attribute attr5 = attr1;

        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
        assertTrue(attr1.equals(attr5));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();

        assertEquals(hashCode1, attr3.hashCode());
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }
}