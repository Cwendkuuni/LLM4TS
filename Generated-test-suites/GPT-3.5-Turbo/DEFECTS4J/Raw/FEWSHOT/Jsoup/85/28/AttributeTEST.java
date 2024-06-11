package org.jsoup.nodes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {
    private Attribute attribute;

    @BeforeEach
    void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("newKey", "newValue");

        assertTrue(attribute.equals(equalAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}