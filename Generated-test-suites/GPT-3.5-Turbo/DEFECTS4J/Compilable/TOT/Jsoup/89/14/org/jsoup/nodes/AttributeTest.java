package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());

        // Edge case: empty key
        Attribute attribute2 = new Attribute("", "value");
        assertEquals("", attribute2.getKey());

        // Error scenario: null key
        try {
            Attribute attribute3 = new Attribute(null, "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // assertion passed
        }
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");

        // Typical use case
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        // Edge case: empty key
        attribute.setKey("");
        assertEquals("", attribute.getKey());

        // Error scenario: null key
        try {
            attribute.setKey(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // assertion passed
        }
    }

    @Test
    public void testGetValue() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());

        // Edge case: null value
        Attribute attribute2 = new Attribute("key", null);
        assertNull(attribute2.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");

        // Typical use case
        String oldVal = attribute.setValue("newValue");
        assertEquals("value", oldVal);
        assertEquals("newValue", attribute.getValue());

        // Edge case: null value
        String oldVal2 = attribute.setValue(null);
        assertEquals("newValue", oldVal2);
        assertNull(attribute.getValue());
    }

    @Test
    public void testHtml() {
        // Typical use case
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());

        // Edge case: attribute value with special characters
        Attribute attribute2 = new Attribute("key", "value \"with\" special <characters>");
        assertEquals("key=\"value &quot;with&quot; special &lt;characters&gt;\"", attribute2.html());
    }

    @Test
    public void testToString() {
        // Typical use case
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.toString());

        // Edge case: attribute value with special characters
        Attribute attribute2 = new Attribute("key", "value \"with\" special <characters>");
        assertEquals("key=\"value &quot;with&quot; special &lt;characters&gt;\"", attribute2.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        // Typical use case
        Attribute attribute = Attribute.createFromEncoded("key", "value%20with%20spaces");
        assertEquals("key", attribute.getKey());
        assertEquals("value with spaces", attribute.getValue());

        // Edge case: empty key and value
        Attribute attribute2 = Attribute.createFromEncoded("", "");
        assertEquals("", attribute2.getKey());
        assertEquals("", attribute2.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        // Typical use case
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());

        // Edge case: non-data attribute
        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        // Typical use case: boolean attribute with empty value
        Attribute attribute = new Attribute("checked", "");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(out));

        // Typical use case: boolean attribute with value same as key
        Attribute attribute2 = new Attribute("checked", "checked");
        assertTrue(attribute2.shouldCollapseAttribute(out));

        // Edge case: boolean attribute with non-empty value
        Attribute attribute3 = new Attribute("checked", "true");
        assertFalse(attribute3.shouldCollapseAttribute(out));

        // Edge case: non-boolean attribute with empty value
        Attribute attribute4 = new Attribute("href", "");
        assertFalse(attribute4.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        // Typical use case: boolean attribute
        assertTrue(Attribute.isBooleanAttribute("checked"));

        // Edge case: non-boolean attribute
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        // Typical use case: same key and value
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));

        // Edge case: different key
        Attribute attribute3 = new Attribute("key", "value");
        Attribute attribute4 = new Attribute("otherKey", "value");
        assertFalse(attribute3.equals(attribute4));

        // Edge case: different value
        Attribute attribute5 = new Attribute("key", "value");
        Attribute attribute6 = new Attribute("key", "otherValue");
        assertFalse(attribute5.equals(attribute6));
    }

    @Test
    public void testHashCode() {
        // Typical use case: same key and value
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1.hashCode(), attribute2.hashCode());

        // Edge case: different key
        Attribute attribute3 = new Attribute("key", "value");
        Attribute attribute4 = new Attribute("otherKey", "value");
        assertNotEquals(attribute3.hashCode(), attribute4.hashCode());

        // Edge case: different value
        Attribute attribute5 = new Attribute("key", "value");
        Attribute attribute6 = new Attribute("key", "otherValue");
        assertNotEquals(attribute5.hashCode(), attribute6.hashCode());
    }

    @Test
    public void testClone() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute, clonedAttribute);
    }
}