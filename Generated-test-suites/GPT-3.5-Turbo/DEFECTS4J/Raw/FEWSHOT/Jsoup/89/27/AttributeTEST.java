
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setup() {
        attribute = new Attribute("name", "value");
    }

    @Test
    public void testConstructor() {
        assertNotNull(attribute);
        assertEquals("name", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyEmpty() {
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        attribute.setValue(null);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("name=\"value\"", html);
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("name", "value");
        assertEquals(attribute, attribute2);
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }
}
