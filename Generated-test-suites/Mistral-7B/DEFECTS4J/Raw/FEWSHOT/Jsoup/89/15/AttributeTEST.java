package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes.NotFound;
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
        assertEquals("value", attrWithParent.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldValue = attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldValue);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        String oldValueWithParent = attrWithParent.setValue("newValue");
        assertEquals("newValue", attrWithParent.getValue());
        assertEquals("value", oldValueWithParent);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Attribute booleanAttr = new Attribute("booleanAttribute", "");
        sb.setLength(0);
        booleanAttr.html(sb, new Document("").outputSettings());
        expectedHtml = "booleanAttribute";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));

        Attribute emptyValAttr = new Attribute("emptyValAttr", "");
        sb.setLength(0);
        emptyValAttr.html(sb, new Document("").outputSettings());
        assertEquals("", StringUtil.releaseBuilder(sb));

        Attribute noValAttr = new Attribute("noValAttr", null);
        sb.setLength(0);
        noValAttr.html(sb, new Document("").outputSettings());
        assertEquals("", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data:key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
        assertTrue(Attribute.shouldCollapseAttribute("boolean", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("boolean", "value", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("boolean", null);
        assertTrue(booleanAttr.isBooleanAttribute());

        Attribute booleanAttrValue = new Attribute("boolean", "true");
        assertTrue(booleanAttrValue.isBooleanAttribute());
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

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}