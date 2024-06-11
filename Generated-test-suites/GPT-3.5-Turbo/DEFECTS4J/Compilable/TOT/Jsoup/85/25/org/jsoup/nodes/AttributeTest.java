package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    // Test case for Attribute constructor with raw key and value
    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test case for Attribute constructor with raw key, value, and parent Attributes
    @Test
    public void testAttributeConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    // Test case for setKey method with valid key
    @Test
    public void testSetKeyValid() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test case for setKey method with null key
    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    // Test case for setValue method with valid value
    @Test
    public void testSetValueValid() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    // Test case for setValue method with null value
    @Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    // Test case for html method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.html());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    // Test case for createFromEncoded method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Test case for isDataAttribute method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    // Test case for shouldCollapseAttribute method
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        assertNotNull(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test case for isBooleanAttribute method
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    // Test case for equals method with equal attributes
    @Test
    public void testEqualsEqualAttributes() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    // Test case for equals method with different key
    @Test
    public void testEqualsDifferentKey() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("newKey", "value");
        assertFalse(attribute1.equals(attribute2));
    }

    // Test case for equals method with different value
    @Test
    public void testEqualsDifferentValue() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "newValue");
        assertFalse(attribute1.equals(attribute2));
    }

    // Test case for hashCode method with equal attributes
    @Test
    public void testHashCodeEqualAttributes() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    // Test case for hashCode method with different key
    @Test
    public void testHashCodeDifferentKey() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("newKey", "value");
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    // Test case for hashCode method with different value
    @Test
    public void testHashCodeDifferentValue() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "newValue");
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    // Test case for clone method
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
    }

}