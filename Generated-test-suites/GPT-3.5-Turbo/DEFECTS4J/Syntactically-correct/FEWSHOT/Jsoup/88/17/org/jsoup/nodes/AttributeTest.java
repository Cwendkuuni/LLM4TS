package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttributeTest {

    private Attribute attribute;
    private String key;
    private String value;
    private Attributes parent;

    @Before
    public void setUp() {
        key = "testKey";
        value = "testValue";
        parent = mock(Attributes.class);
        attribute = new Attribute(key, value, parent);
    }

    @Test
    public void testGetKey() {
        assertEquals(key, attribute.getKey());
    }

    @Test
    public void testSetKey() {
        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
        assertEquals(newKey, attribute.parent.keys[0]);
    }

    @Test
    public void testSetKeyWithParentNull() {
        Attribute attribute = new Attribute(key, value);
        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        attribute.setKey(null);
    }

    @Test
    public void testGetValue() {
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String newValue = "newValue";
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(newValue, attribute.parent.vals[0]);
    }

    @Test
    public void testSetValueWithParentNull() {
        Attribute attribute = new Attribute(key, value);
        String newValue = "newValue";
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
    }

    @Test
    public void testToString() {
        String expected = key + "=\"" + value + "\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute(key, value, parent);
        Attribute differentAttribute = new Attribute("differentKey", value, parent);
        assertEquals(attribute, sameAttribute);
        assertNotEquals(attribute, differentAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute(key, value, parent);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertNotSame(attribute, clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}