package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", null);
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNull(attrWithParent.parent);

        Attribute attrWithParent1 = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent1.getKey());
        assertEquals("value", attrWithParent1.getValue());
        assertNotNull(attrWithParent1.parent);
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
        String oldVal = attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldVal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
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
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("html", "");
        assertTrue(attr.shouldCollapseAttribute());

        Attribute attr1 = new Attribute("checked", "");
        assertTrue(attr1.shouldCollapseAttribute());

        Attribute attr2 = new Attribute("class", "foo");
        assertFalse(attr2.shouldCollapseAttribute());

        Attribute attr3 = new Attribute("title", "foo");
        assertFalse(attr3.shouldCollapseAttribute());

        Attribute attr4 = new Attribute("style", "color: red");
        assertFalse(attr4.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        Attribute attr1 = new Attribute("class", "foo");
        assertFalse(attr1.isBooleanAttribute());

        Attribute attr2 = new Attribute("title", "foo");
        assertFalse(attr2.isBooleanAttribute());

        Attribute attr3 = new Attribute("style", "color: red");
        assertFalse(attr3.isBooleanAttribute());

        Attribute attr4 = new Attribute("data-foo", "bar");
        assertTrue(attr4.isBooleanAttribute());

        Attribute attr5 = new Attribute("data-foo", null);
        assertTrue(attr5.isBooleanAttribute());
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertTrue(attr1.hashCode() == attr3.hashCode());

        assertFalse(attr1.equals(attr2));
        assertFalse(attr1.hashCode() == attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();

        assertEquals(attr.getKey(), clone.getKey());
        assertEquals(attr.getValue(), clone.getValue());
        assertNull(clone.parent);
    }
}