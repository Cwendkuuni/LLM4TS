package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class AttributeTest {

    @Test
    public void testCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertSame(attrWithParent.parent, attrWithParent.getParent());
    }

    @Test
    public void testValidation() {
        Validate.assertNull(new Attribute(null, "value"));
        Validate.assertNull(new Attribute("", "value"));
        Validate.assertNull(new Attribute("key", null));
        Validate.assertNull(new Attribute("key", "", new Attributes()));

        Attribute attr = new Attribute("key", "value");
        Validate.assertNull(attr.parent);

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Validate.assertNotNull(attrWithParent.parent);
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key1", "value1");
        attr.setKey("key2");
        assertEquals("key2", attr.getKey());

        Attribute attrWithParent = new Attribute("key1", "value1", new Attributes());
        attrWithParent.setKey("key2");
        assertEquals("key2", attrWithParent.getKey());
        assertSame(attrWithParent.parent, attrWithParent.getParent());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue("value2");
        assertEquals("value2", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value1", new Attributes());
        attrWithParent.setValue("value2");
        assertEquals("value2", attrWithParent.getValue());
        assertSame(attrWithParent.parent, attrWithParent.getParent());
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attr.html(sb2, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        StringBuilder sb3 = StringUtil.borrowBuilder();
        attrWithParent.html(sb3, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb3));
        assertSame(attrWithParent.parent, attrWithParent.getParent());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key1", "value2");
        Attribute attr4 = new Attribute("key2", "value1");

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key1", "value2");
        Attribute attr4 = new Attribute("key2", "value1");

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();
        int hashCode4 = attr4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals("key", clonedAttr.getKey());
        assertEquals("value", clonedAttr.getValue());
        assertNull(clonedAttr.parent);
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(new Attribute("key", null).isBooleanAttribute());
        assertFalse(new Attribute("key", "").isBooleanAttribute());
        assertFalse(new Attribute("key", "value").isBooleanAttribute());
        assertTrue(new Attribute("checked", null).isBooleanAttribute());
        assertTrue(new Attribute("checked", "").isBooleanAttribute());
        assertTrue(new Attribute("checked", "false").isBooleanAttribute());
        assertTrue(new Attribute("checked", "true").isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", null);
        assertTrue(attr.isDataAttribute());

        attr.setKey("data:key");
        assertTrue(attr.isDataAttribute());

        attr.setValue("value");
        assertTrue(attr.isDataAttribute());

        attr.setKey("nonData:key");
        assertFalse(attr.isDataAttribute());
    }
}