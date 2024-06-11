package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value2");
        Attribute attr3 = new Attribute("key1", null);
        Attribute attr4 = new Attribute("key1", "", new Attributes());

        assertEquals("key1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        assertEquals("key1", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        assertNull(attr3.getValue());
        assertEquals("key1", attr4.getKey());
        assertNull(attr4.getValue());
    }

    @Test
    public void testAttributeSetters() {
        Attribute attr = new Attribute("key1", "value1");

        attr.setKey("key2");
        assertEquals("key2", attr.getKey());

        attr.setValue("value2");
        assertEquals("key2", attr.getKey());
        assertEquals("value2", attr.getValue());
    }

    @Test
    public void testCollapsibleAttributes() {
        Document.OutputSettings outputSettings = Document.OutputSettings.html();

        Attribute attr1 = new Attribute("booleanAttribute", "");
        Attribute attr2 = new Attribute("booleanAttribute", "true");
        Attribute attr3 = new Attribute("nonBooleanAttribute", "value");

        assertTrue(attr1.shouldCollapseAttribute(outputSettings));
        assertFalse(attr2.shouldCollapseAttribute(outputSettings));
        assertFalse(attr3.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr1 = new Attribute("booleanAttribute", "");
        Attribute attr2 = new Attribute("nonBooleanAttribute", "value");

        assertTrue(attr1.isBooleanAttribute());
        assertFalse(attr2.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr1 = new Attribute("data-test", "value");
        Attribute attr2 = new Attribute("normalAttribute", "value");

        assertTrue(attr1.isDataAttribute());
        assertFalse(attr2.isDataAttribute());
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

        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key1", "value1");

        Attribute clonedAttr = attr.clone();

        assertEquals(attr.getKey(), clonedAttr.getKey());
        assertEquals(attr.getValue(), clonedAttr.getValue());
    }
}