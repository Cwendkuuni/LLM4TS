package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    @Test
    public void testBasicAttributeSetting() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertNull(attributes.get("unknownKey"));
    }

    @Test
    public void testCaseInsensitiveAttributeSetting() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
        Assert.assertNull(attributes.getIgnoreCase("unknownKey"));
    }

    @Test
    public void testBooleanAttributeSetting() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", "true");
        Assert.assertTrue(attributes.getBoolean("booleanKey"));
        Assert.assertFalse(attributes.getBoolean("booleanKey").equals("false"));
        Assert.assertNull(attributes.getBoolean("unknownKey"));

        Attributes booleanAttributes = new Attributes();
        booleanAttributes.put("booleanKey", "false");
        Assert.assertFalse(booleanAttributes.getBoolean("booleanKey"));
        Assert.assertNull(booleanAttributes.getBoolean("unknownKey"));

        Attributes emptyAttributes = new Attributes();
        Assert.assertFalse(emptyAttributes.getBoolean("booleanKey"));
    }

    @Test
    public void testAttributeRemoval() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertNull(attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsAccess() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.get(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidGrowthFactor() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(1);
        attributes.put("key", "value");
        attributes.checkCapacity(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullIteration() {
        Attributes attributes = new Attributes();
        Iterator<Attribute> iterator = attributes.iterator();
        iterator.next();
        iterator = null;
        iterator.hasNext();
    }

    @Test
    public void testAttributesEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testAttributesClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        Assert.assertEquals(attributes, cloned);
    }

    @Test
    public void testAttributesNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.normalize();
        Assert.assertEquals("key1", attributes.get("Key1"));
    }
}