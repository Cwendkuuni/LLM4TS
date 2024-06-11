package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
        assertTrue(parent.contains("key"));
        assertEquals("value", parent.get("key"));
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
        String oldValue = attr.getValue();
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertNotEquals(oldValue, attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb);
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        Document doc = new Document();
        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, doc.outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));
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
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        Attribute boolAttr = new Attribute("checked", "");
        assertTrue(boolAttr.shouldCollapseAttribute(null));

        Attribute boolAttrValue = new Attribute("checked", "checked");
        assertTrue(boolAttrValue.shouldCollapseAttribute(null));

        Attribute notBoolAttr = new Attribute("key", "notValue");
        assertFalse(notBoolAttr.shouldCollapseAttribute(null));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
    }
}