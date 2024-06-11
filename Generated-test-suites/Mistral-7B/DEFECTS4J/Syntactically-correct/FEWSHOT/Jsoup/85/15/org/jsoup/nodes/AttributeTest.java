package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-test", "value");
        assertTrue(attr.isDataAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        assertFalse(booleanAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute htmlAttribute = new Attribute("checked", "checked");
        assertTrue(htmlAttribute.shouldCollapseAttribute(null));

        Attribute htmlBooleanAttribute = new Attribute("checked", "");
        assertTrue(htmlBooleanAttribute.shouldCollapseAttribute(null));

        Attribute dataAttribute = new Attribute("data-test", "value");
        assertFalse(dataAttribute.shouldCollapseAttribute(null));

        Attribute emptyDataAttribute = new Attribute("data-test", "");
        assertTrue(emptyDataAttribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("data-test", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key2", "value2");
        Attribute attr4 = new Attribute("key", "value2");
        Attribute attr5 = attr1;

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr5);
        assertNotEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1, attr4);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr2.hashCode();
        int hashCode3 = attr3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute originalAttr = new Attribute("key", "value");
        Attribute clonedAttr = originalAttr.clone();

        assertEquals(originalAttr, clonedAttr);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("test", "data-test=\"value\"");
        assertEquals("test", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedNullKey() {
        Attribute.createFromEncoded(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedNullEncodedValue() {
        Attribute.createFromEncoded("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedEmptyEncodedValue() {
        Attribute.createFromEncoded("key", "");
    }
}