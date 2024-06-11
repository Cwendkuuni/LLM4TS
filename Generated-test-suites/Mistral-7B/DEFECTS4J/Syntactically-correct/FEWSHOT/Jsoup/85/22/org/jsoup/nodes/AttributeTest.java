package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testKeySetting() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");

        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test
    public void testValueSetting() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");

        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);

        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("html", null, new Document.OutputSettings());
        assertFalse(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings());
        assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("checked", "", new Document.OutputSettings());
        assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("checked", "checked", new Document.OutputSettings());
        assertTrue(shouldCollapse);

        shouldCollapse = Attribute.shouldCollapseAttribute("checked", "false", new Document.OutputSettings());
        assertFalse(shouldCollapse);
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

        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
    }
}