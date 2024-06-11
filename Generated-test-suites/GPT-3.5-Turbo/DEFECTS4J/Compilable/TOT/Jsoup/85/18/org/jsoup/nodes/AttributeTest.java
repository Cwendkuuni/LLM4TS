package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals(attribute.html(), attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encoded%3DValue");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encoded=Value", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "data-value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("test"));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute, equalAttribute);
        assertNotEquals(attribute, null);
        assertNotEquals(attribute, new Attribute("differentKey", "value"));
        assertNotEquals(attribute, new Attribute("key", "differentValue"));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}