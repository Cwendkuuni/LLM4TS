package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute parentAttr = new Attribute("parentKey", "parentValue", new Attributes(new Attributes.Attribute[]{attr}));
        attr.setParent(parentAttr);
        assertEquals(parentAttr, attr.getParent());
        assertEquals(1, parentAttr.size());
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", "true");
        assertTrue(attr.isBooleanAttribute());
        assertTrue(attr.isDataAttribute());
        assertTrue(attr.isDataAttribute(true));
        assertFalse(attr.isDataAttribute(false));
        assertFalse(attr.shouldCollapseAttribute());

        Attribute attrFalse = new Attribute("checked", null);
        assertFalse(attrFalse.isBooleanAttribute());
        assertFalse(attrFalse.isDataAttribute());
        assertFalse(attrFalse.isDataAttribute(true));
        assertTrue(attrFalse.isDataAttribute(false));
        assertTrue(attrFalse.shouldCollapseAttribute());
    }

    @Test
    public void testCollapsibleAttributes() {
        Attribute htmlAttr = new Attribute("hidden", "hidden");
        assertTrue(htmlAttr.shouldCollapseAttribute());

        Attribute styleAttr = new Attribute("style", "display: none;");
        assertFalse(styleAttr.shouldCollapseAttribute());

        Attribute inputTypeAttr = new Attribute("type", "text");
        assertFalse(inputTypeAttr.shouldCollapseAttribute());

        Attribute inputTypeAttrEmpty = new Attribute("type", "");
        assertTrue(inputTypeAttrEmpty.shouldCollapseAttribute());

        Attribute inputTypeAttrSame = new Attribute("type", "text");
        assertTrue(inputTypeAttrSame.shouldCollapseAttribute());
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertEquals(attr.hashCode(), clonedAttr.hashCode());
    }
}