package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(-1, parent.indexOfKey("key"));
        assertEquals(0, parent.indexOfKey("newKey"));
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", attribute.setValue("oldValue"));
        assertEquals(-1, parent.indexOfKey("key"));
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", newAttribute.getKey());
        assertEquals("value", newAttribute.getValue());
        assertNull(newAttribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("hidden"));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value", parent);
        Attribute differentAttribute = new Attribute("differentKey", "value", parent);

        assertTrue(attribute.equals(equalAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value", parent);
        Attribute differentAttribute = new Attribute("differentKey", "value", parent);

        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();

        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNull(clonedAttribute.parent);
    }
}