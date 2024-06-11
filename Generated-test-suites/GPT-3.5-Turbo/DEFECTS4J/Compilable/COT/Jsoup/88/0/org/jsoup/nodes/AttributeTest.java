package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    // Method: getKey()
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Method: setKey(String key)
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Method: getValue()
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Method: setValue(String val)
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    // Method: html()
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    // Method: toString()
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    // Method: createFromEncoded(String unencodedKey, String encodedValue)
    // Edge case: null unencoded key
    @Test(expected = NullPointerException.class)
    public void testCreateFromEncodedWithNullKey() {
        Attribute.createFromEncoded(null, "encodedValue");
    }

    // Method: createFromEncoded(String unencodedKey, String encodedValue)
    // Edge case: null encoded value
    @Test
    public void testCreateFromEncodedWithNullValue() {
        Attribute attribute = Attribute.createFromEncoded("key", null);
        assertNull(attribute.getValue());
    }

    // Method: createFromEncoded(String unencodedKey, String encodedValue)
    // Exception handling: invalid encoded value
    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromEncodedWithInvalidValue() {
        Attribute.createFromEncoded("key", "&#invalid;");
    }

    // Method: isDataAttribute()
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    // Method: shouldCollapseAttribute(Document.OutputSettings out)
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Method: isBooleanAttribute()
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    // Method: equals(Object o)
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    // Method: hashCode()
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(366101973, attribute.hashCode());
    }

    // Method: clone()
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}