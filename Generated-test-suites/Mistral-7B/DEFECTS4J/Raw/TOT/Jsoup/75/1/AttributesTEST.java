package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetAttribute() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.get("key2"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        attributes.put("Key1", "Value1");
        Assert.assertEquals("Value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(Attributes.EMPTY_STRING, attributes.getIgnoreCase("key3"));
    }

    @Test
    public void testPutAttribute() {
        attributes.put("key1", "value1");
        attributes.put("key1", "newValue1");
        Assert.assertEquals("newValue1", attributes.get("key1"));

        attributes.put("key2", "value2");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "newValue1");
        map.put("key2", "value2");
        Assert.assertEquals(map, attributes.asMap());
    }

    @Test
    public void testPutAttributeIgnoreCase() {
        attributes.put("Key1", "Value1");
        attributes.put("Key1", "newValue1");
        Assert.assertEquals("newValue1", attributes.getIgnoreCase("key1"));

        attributes.put("Key2", "Value2");
        Map<String, String> map = new HashMap<>();
        map.put("Key1", "newValue1");
        map.put("Key2", "Value2");
        Assert.assertEquals(map, attributes.asMap());
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("key1", "true");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.getBoolean("key1")); // getBoolean() returns false for non-null strings
        attributes.put("key1", "false");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.getBoolean("key1"));
        attributes.put("key1", null);
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutIgnoreCaseBooleanAttribute() {
        attributes.put("Key1", "TRUE");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.getBooleanIgnoreCase("key1")); // getBooleanIgnoreCase() returns false for non-null strings
        attributes.put("Key1", "FALSE");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.getBooleanIgnoreCase("key1"));
        attributes.put("Key1", null);
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        Assert.assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("Key1", "Value1");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes.addAll(attributes2);
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        expected.put("key4", "value4");
        Assert.assertEquals(expected, attributes.asMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "Value1");
        attributes.removeIgnoreCase("key1");
        Assert.assertFalse(attributes.hasKey("Key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringValue() {
        attributes.put("key1", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        attributes.put("key1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseDuplicateKey() {
        attributes.put("Key1", "Value1");
        attributes.put("Key1", "Value2");
    }
}