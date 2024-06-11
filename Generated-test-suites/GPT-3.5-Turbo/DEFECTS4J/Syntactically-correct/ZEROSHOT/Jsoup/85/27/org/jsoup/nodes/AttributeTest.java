package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setup() {
        parent = new Attributes();
        attribute = new Attribute("testKey", "testValue", parent);
    }

    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assertEquals("testKey", key);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assertEquals("newKey", key);
    }

    @Test
    public void testGetValue() {
        String value = attribute.getValue();
        assertEquals("testValue", value);
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assertEquals("newValue", value);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("testKey=\"testValue\"", html);
    }

    @Test
    public void testToString() {
        String toString = attribute.toString();
        assertEquals("testKey=\"testValue\"", toString);
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }
}