package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testNewAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(parent, attr.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attr.html(expected, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(expected));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("disabled"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedKey = "data-my-attribute";
        String decodedKey = Attributes.dataPrefix + "my-attribute";
        Attribute attr = Attribute.createFromEncoded(decodedKey, "value");
        assertEquals(decodedKey, attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(null));
        assertFalse(attr1.equals(""));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr1.hashCode();
        int hashCode3 = attr2.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}