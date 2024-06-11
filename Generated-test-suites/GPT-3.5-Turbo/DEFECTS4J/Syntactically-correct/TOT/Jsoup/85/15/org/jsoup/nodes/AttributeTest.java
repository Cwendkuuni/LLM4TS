import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    // Test cases for getKey() method

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    // Test cases for setKey() method

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetKey_NullKey() {
        Attribute attribute = new Attribute("key", "value");
        try {
            attribute.setKey(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key must not be null", e.getMessage());
        }
    }

    // Test cases for getValue() method

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    // Test cases for setValue() method

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValue_NullValue() {
        Attribute attribute = new Attribute("key", "value");
        try {
            attribute.setValue(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("val must not be null", e.getMessage());
        }
    }

    // Test cases for html() method

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test cases for toString() method

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    // Test cases for createFromEncoded() method

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }
    
    // Test cases for isDataAttribute() method
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttribute_NotDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    // Test cases for shouldCollapseAttribute() method

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "key");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testShouldCollapseAttribute_BooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttribute_NotCollapsible() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(out));
    }

    // Test cases for isBooleanAttribute() method

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute_BooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    // Test cases for equals() method

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testEquals_NullKey() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute(null, "value");
        assertFalse(attribute1.equals(attribute2));
    }

    // Test cases for hashCode() method

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals(-819279830, attribute.hashCode());
    }

    // Test cases for clone() method

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute, cloneAttribute);
    }

}