package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setup() {
        attribute = new Attribute("key", "value");
    }

    // Test getKey method with typical use cases
    @Test
    public void testGetKey_typical() {
        assertEquals("key", attribute.getKey());
    }

    // Test getKey method with edge case of empty key
    @Test
    public void testGetKey_emptyKey() {
        attribute.setKey("");
        assertEquals("", attribute.getKey());
    }

    // Test setKey method with typical use case
    @Test
    public void testSetKey_typical() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test setKey method with edge case of empty key
    @Test
    public void testSetKey_emptyKey() {
        attribute.setKey("");
        assertEquals("", attribute.getKey());
    }

    // Test get value method with typical use cases
    @Test
    public void testGetValue_typical() {
        assertEquals("value", attribute.getValue());
    }

    // Test setValue method with typical use cases
    @Test
    public void testSetValue_typical() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    // Test html method with typical use cases
    @Test
    public void testHtml_typical() {
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test createFromEncoded method with typical use cases
    @Test
    public void testCreateFromEncoded_typical() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    // Test isDataAttribute method with typical use case
    @Test
    public void testIsDataAttribute_typical() {
        assertFalse(attribute.isDataAttribute());
    }

    // Test shouldCollapseAttribute method with typical use case
    @Test
    public void testShouldCollapseAttribute_typical() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test isBooleanAttribute method with typical use cases
    @Test
    public void testIsBooleanAttribute_typical() {
        assertFalse(attribute.isBooleanAttribute());
    }

    // Test equals method with typical use cases
    @Test
    public void testEquals_typical() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentValueAttribute = new Attribute("key", "different");
        Attribute nullValueAttribute = new Attribute("key", null);

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentValueAttribute));
        assertFalse(attribute.equals(nullValueAttribute));
    }

    // Test hashCode method with typical use case
    @Test
    public void testHashCode_typical() {
        Attribute sameAttribute = new Attribute("key", "value");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    // Test clone method with typical use case
    @Test
    public void testClone_typical() throws CloneNotSupportedException {
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}