package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.Arrays;

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
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(Attribute.isBooleanAttribute(attr.getKey()));
        assertEquals("checked", attr.getKey());
        assertNull(attr.getValue());

        Attribute boolAttrWithValue = new Attribute("checked", "true");
        assertTrue(Attribute.isBooleanAttribute(boolAttrWithValue.getKey()));
        assertEquals("checked", boolAttrWithValue.getKey());
        assertEquals("true", boolAttrWithValue.getValue());

        Attribute nonBoolAttr = new Attribute("key", "value");
        assertFalse(Attribute.isBooleanAttribute(nonBoolAttr.getKey()));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute());

        Attribute booleanAttr = new Attribute("checked", "");
        assertTrue(booleanAttr.shouldCollapseAttribute());

        Attribute booleanAttrWithValue = new Attribute("checked", "true");
        assertTrue(booleanAttrWithValue.shouldCollapseAttribute());

        Attribute dataPrefixAttr = new Attribute("data-key", "value");
        assertTrue(dataPrefixAttr.shouldCollapseAttribute());

        Attribute dataPrefixAttrWithValue = new Attribute("data-key", "value-value");
        assertTrue(dataPrefixAttrWithValue.shouldCollapseAttribute());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = Attribute.createFromEncoded("key", "key=\"value\"", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        assertNotNull(attrWithParent.getParent());
    }

    @Test
    public void testEqualsHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        Attribute attr4 = attr1;

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));

        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }
}