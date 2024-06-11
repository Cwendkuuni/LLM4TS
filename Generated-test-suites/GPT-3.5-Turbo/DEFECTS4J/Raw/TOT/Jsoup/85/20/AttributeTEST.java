import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        // Edge case - empty key and value
        Attribute emptyAttribute = new Attribute("", "");
        assertEquals("", emptyAttribute.getKey());
        assertEquals("", emptyAttribute.getValue());

        // Error scenario - null key and value
        try {
            Attribute nullAttribute = new Attribute(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("oldKey", "value");

        // Typical use case
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        // Edge case - empty key
        attribute.setKey("");
        assertEquals("", attribute.getKey());

        // Error scenario - null key
        try {
            attribute.setKey(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "oldValue");

        // Typical use case
        String oldValue = attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("oldValue", oldValue);

        // Edge case - null value
        attribute.setValue(null);
        assertNull(attribute.getValue());

        // Error scenario - parent is null
        try {
            attribute = new Attribute("key", "value");
            attribute.setValue("newValue");
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());

        // Test IOException scenario
        attribute = new Attribute("key", "value") {
            @Override
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        try {
            attribute.html();
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Exception thrown as expected
            assertEquals("Test IOException", e.getCause().getMessage());
        }
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expectedString = "key=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assertEquals("key", attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        // Typical use case
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());

        // Edge case - key with data prefix of length 1
        attribute = new Attribute("d", "value");
        assertFalse(attribute.isDataAttribute());

        // Edge case - key with data prefix of length 0
        attribute = new Attribute("", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attribute.shouldCollapseAttribute(out));

        // Edge case - null value
        attribute = new Attribute("key", null);
        assertTrue(attribute.shouldCollapseAttribute(out));

        // Edge case - empty value
        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(out));

        // Edge case - value same as key (case-sensitive)
        attribute = new Attribute("key", "key");
        assertTrue(attribute.shouldCollapseAttribute(out));

        // Edge case - value same as key (case-insensitive)
        attribute = new Attribute("key", "KEY");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        // Typical use case
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("key"));

        // Edge case - null key
        assertFalse(Attribute.isBooleanAttribute(null));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "otherValue");
        Attribute attribute4 = new Attribute("otherKey", "value");

        // Typical use case
        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));

        // Null comparison
        assertFalse(attribute1.equals(null));

        // Different class comparison
        assertFalse(attribute1.equals("string"));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "otherValue");
        Attribute attribute4 = new Attribute("otherKey", "value");

        // Typical use case
        assertEquals(attribute1.hashCode(), attribute1.hashCode());
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
    }

    @Test
    public void testClone() {
        // Typical use case
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
        assertNotSame(attribute, clone);
    }
}