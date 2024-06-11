package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    // Test cases for getKey() method

    @Test
    public void getKey_shouldReturnAttributeKey() {
        assertEquals("key", attribute.getKey());
    }

    // Test cases for setKey() method

    @Test
    public void setKey_shouldUpdateAttributeKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKey_shouldThrowExceptionForNullKey() {
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKey_shouldThrowExceptionForEmptyKey() {
        attribute.setKey("");
    }

    // Test cases for getValue() method

    @Test
    public void getValue_shouldReturnAttributeValue() {
        assertEquals("value", attribute.getValue());
    }

    // Test cases for setValue() method

    @Test
    public void setValue_shouldUpdateAttributeValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    // Test cases for html() method

    @Test
    public void html_shouldReturnHtmlRepresentation() {
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test cases for toString() method

    @Test
    public void toString_shouldReturnStringRepresentation() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    // Test cases for createFromEncoded() method

    @Test
    public void createFromEncoded_shouldReturnAttributeWithUnencodedKeyAndDecodedValue() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "&lt;value&gt;");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("<value>", encodedAttribute.getValue());
    }

    // Test cases for isDataAttribute() method

    @Test
    public void isDataAttribute_shouldReturnTrueForDataAttribute() {
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void isDataAttribute_shouldReturnFalseForNonDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertFalse(Attribute.isDataAttribute("key"));
    }

    // Test cases for shouldCollapseAttribute() method

    @Test
    public void shouldCollapseAttribute_shouldReturnTrueForBooleanAttributeWithValueSameAsName() {
        Attribute booleanAttribute = new Attribute("checked", "checked");
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "checked", new Document.OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_shouldReturnTrueForBooleanAttributeWithEmptyValue() {
        Attribute emptyBooleanAttribute = new Attribute("checked", "");
        assertTrue(emptyBooleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(Attribute.shouldCollapseAttribute("checked", "", new Document.OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_shouldReturnFalseForNonBooleanAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings()));
    }

    // Test cases for isBooleanAttribute() method

    @Test
    public void isBooleanAttribute_shouldReturnTrueForBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", "checked");
        assertTrue(booleanAttribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void isBooleanAttribute_shouldReturnTrueForBooleanAttributeWithNullValue() {
        Attribute nullBooleanAttribute = new Attribute("checked", null);
        assertTrue(nullBooleanAttribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void isBooleanAttribute_shouldReturnFalseForNonBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    // Test cases for equals() method

    @Test
    public void equals_shouldReturnTrueForSameObject() {
        assertTrue(attribute.equals(attribute));
    }

    @Test
    public void equals_shouldReturnTrueForEqualAttributes() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(equalAttribute));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentKeys() {
        Attribute differentKeyAttribute = new Attribute("differentKey", "value");
        assertFalse(attribute.equals(differentKeyAttribute));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentValues() {
        Attribute differentValueAttribute = new Attribute("key", "differentValue");
        assertFalse(attribute.equals(differentValueAttribute));
    }

    @Test
    public void equals_shouldReturnFalseForNullAttribute() {
        assertFalse(attribute.equals(null));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentClass() {
        assertFalse(attribute.equals("notAnAttribute"));
    }

    // Test cases for hashCode() method

    @Test
    public void hashCode_shouldReturnSameHashCodeForEqualAttributes() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    // Test case for clone() method

    @Test
    public void clone_shouldReturnClonedAttribute() {
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}