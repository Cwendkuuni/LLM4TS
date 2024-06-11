package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
    private Attribute attribute1;
    private Attribute attribute2;
    private Attribute attribute3;

    @Before
    public void setup() {
        attribute1 = new Attribute("key1", "value1");
        attribute2 = new Attribute("key2", "value2");
        attribute3 = new Attribute("key3", "value3");
    }

    @Test
    public void testGetKey() {
        assertEquals("key1", attribute1.getKey());
        assertEquals("key2", attribute2.getKey());
        assertEquals("key3", attribute3.getKey());
    }

    @Test
    public void testSetKey() {
        attribute1.setKey("newKey1");
        attribute2.setKey("newKey2");
        attribute3.setKey("newKey3");

        assertEquals("newKey1", attribute1.getKey());
        assertEquals("newKey2", attribute2.getKey());
        assertEquals("newKey3", attribute3.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value1", attribute1.getValue());
        assertEquals("value2", attribute2.getValue());
        assertEquals("value3", attribute3.getValue());
    }

    @Test
    public void testSetValue() {
        attribute1.setValue("newValue1");
        attribute2.setValue("newValue2");
        attribute3.setValue("newValue3");

        assertEquals("newValue1", attribute1.getValue());
        assertEquals("newValue2", attribute2.getValue());
        assertEquals("newValue3", attribute3.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key1=\"value1\"", attribute1.html());
        assertEquals("key2=\"value2\"", attribute2.html());
        assertEquals("key3=\"value3\"", attribute3.html());
    }

    @Test
    public void testToString() {
        assertEquals("key1=\"value1\"", attribute1.toString());
        assertEquals("key2=\"value2\"", attribute2.toString());
        assertEquals("key3=\"value3\"", attribute3.toString());
    }

    @Test
    public void testEquals() {
        Attribute attribute1Copy = new Attribute("key1", "value1");
        Attribute attribute2Copy = new Attribute("key2", "value2");
        Attribute attribute3Copy = new Attribute("key3", "value3");

        assertEquals(attribute1, attribute1Copy);
        assertEquals(attribute2, attribute2Copy);
        assertEquals(attribute3, attribute3Copy);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1Copy = new Attribute("key1", "value1");
        Attribute attribute2Copy = new Attribute("key2", "value2");
        Attribute attribute3Copy = new Attribute("key3", "value3");

        assertEquals(attribute1.hashCode(), attribute1Copy.hashCode());
        assertEquals(attribute2.hashCode(), attribute2Copy.hashCode());
        assertEquals(attribute3.hashCode(), attribute3Copy.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone1 = attribute1.clone();
        Attribute clone2 = attribute2.clone();
        Attribute clone3 = attribute3.clone();

        assertEquals(attribute1, clone1);
        assertEquals(attribute2, clone2);
        assertEquals(attribute3, clone3);
    }
}