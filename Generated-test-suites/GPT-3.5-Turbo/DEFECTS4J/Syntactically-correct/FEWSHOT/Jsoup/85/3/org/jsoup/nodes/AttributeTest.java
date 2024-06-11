
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute(KEY, VALUE);
        assertEquals(KEY, attribute.getKey());
        assertEquals(VALUE, attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute(KEY, VALUE);
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute(KEY, VALUE);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHTML() {
        Attribute attribute = new Attribute(KEY, VALUE);
        String html = attribute.html();
        assertNotNull(html);
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute(KEY, VALUE);
        String toString = attribute.toString();
        assertNotNull(toString);
        assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded(KEY, "encodedValue");
        assertEquals(KEY, attribute.getKey());
        assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", VALUE);
        assertTrue(attribute.isDataAttribute());
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute(KEY, VALUE);
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(Attribute.shouldCollapseAttribute(KEY, VALUE, new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute(KEY, VALUE);
        Attribute attribute2 = new Attribute(KEY, VALUE);
        Attribute attribute3 = new Attribute("newKey", VALUE);
        Attribute attribute4 = new Attribute(KEY, "newValue");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute(KEY, VALUE);
        Attribute attribute2 = new Attribute(KEY, VALUE);
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute(KEY, VALUE);
        Attribute clone = attribute.clone();
        assertNotNull(clone);
        assertEquals(attribute.getClass(), clone.getClass());
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}
