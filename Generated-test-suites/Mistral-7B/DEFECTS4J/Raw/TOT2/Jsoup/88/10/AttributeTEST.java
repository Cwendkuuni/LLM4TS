
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    // Test cases for Attribute constructor
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationWithNullKey() {
        try {
            new Attribute(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAttributeCreationWithEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        assertNotNull(attribute);
        assertEquals("", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationWithNullValue() {
        Attribute attribute = new Attribute("key", null);
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertNull(attribute.getValue());
    }

    // Test cases for toString() method
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testToStringWithEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        String expected = "\"\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testToStringWithEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        String expected = "key=\"\"";
        assertEquals(expected, attribute.toString());
    }

    @Test(expected = SerializationException.class)
    public void testToStringWithIOException() {
        Attribute attribute = new Attribute("key", "value");
        // Intentionally cause an IOException in the html() method
        StringUtil.borrowBuilder().html(attribute.key, attribute.val, StringUtil.borrowBuilder(), null);
    }

    // Test cases for setValue() method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        assertEquals("value", attribute.getValue());
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
        assertNull(attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullParent() {
        Attribute attribute = new Attribute("key", "value");
        try {
            attribute.setValue("newValue", null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSetValueWithExistingKey() {
        Attributes parent = new Attributes();
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key", "value");
        parent.put(attribute1);
        parent.put(attribute2);
        try {
            attribute2.setValue("newValue");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}
