package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    /**
     * Test the creation of a new attribute from unencoded key and value.
     */
    @Test
    public void testCreateAttributeFromUnencodedKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    /**
     * Test the creation of a new attribute from unencoded key, value, and parent attributes.
     */
    @Test
    public void testCreateAttributeFromUnencodedKeyAndValueAndParentAttributes() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertSame(parentAttributes, attribute.parent);
    }

    /**
     * Test setting a new key.
     */
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    /**
     * Test setting a new key to an existing attribute in parent attributes.
     */
    @Test
    public void testSetKeyToExistingAttributeInParentAttributes() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        parentAttributes.put("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("value", parentAttributes.get("newKey"));
    }

    /**
     * Test setting a new value.
     */
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    /**
     * Test setting a new value to an existing attribute in parent attributes.
     */
    @Test
    public void testSetValueToExistingAttributeInParentAttributes() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        parentAttributes.put("key", "oldValue");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parentAttributes.get("key"));
    }

    /**
     * Test generating the HTML representation of an attribute.
     */
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    /**
     * Test creating a new attribute from an unencoded key and an HTML attribute encoded value.
     */
    @Test
    public void testCreateAttributeFromEncodedValue() {
        Attribute attribute = Attribute.createFromEncoded("key", "amp&amp;");
        assertEquals("key", attribute.getKey());
        assertEquals("amp&", attribute.getValue());
    }

    /**
     * Test checking if an attribute is a data attribute.
     */
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    /**
     * Test checking if an attribute key is defined as a boolean attribute in HTML5.
     */
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    /**
     * Test checking if an attribute should collapse based on output settings.
     */
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    /**
     * Test the equals method of an attribute.
     */
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
    }

    /**
     * Test the hashCode method of an attribute.
     */
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int expectedHashCode = 31 * "key".hashCode() + "value".hashCode();
        assertEquals(expectedHashCode, attribute.hashCode());
    }

    /**
     * Test cloning an attribute.
     */
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute, clonedAttribute);
    }
}