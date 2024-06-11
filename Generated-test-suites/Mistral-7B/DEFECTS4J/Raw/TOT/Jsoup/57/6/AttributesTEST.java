package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        String value = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String value = attributes.get("key");
        Assert.assertEquals("value", value);

        attributes.put("key", "newValue");
        value = attributes.get("key");
        Assert.assertEquals("newValue", value);
    }

    @Test
    public void testPutWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        String value = attributes.get("key");
        Assert.assertEquals("", value);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String value = attributes.getIgnoreCase("key");
        Assert.assertEquals("Value", value);
    }

    @Test
    public void testPutWithBooleanValueTrue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testPutWithBooleanValueFalse() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "newValue");
        String value = attributes.get("key");
        Assert.assertEquals("newValue", value);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String value = attributes.get("key");
        Assert.assertEquals("", value);
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        String value = attributes.get("Key");
        Assert.assertEquals("Value", value);
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));

        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));

        attributes.put("Key", "value");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));

        attributes.put("Key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        attributes.put("Key", "value");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);
        Assert.assertEquals(3, attributes1.size());
        Assert.assertEquals("value3", attributes1.get("key2"));
        Assert.assertEquals("value3", attributes1.getIgnoreCase("key2"));
        Assert.assertEquals("value3", attributes1.get("KEY2"));
        Assert.assertEquals("value3", attributes1.getIgnoreCase("Key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("data-key"));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        String html = attributes.html();
    }

    @Test
    public void testHtmlWithAttributes() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String html = attributes.html();
        Assert.assertEquals("<key>value</key>", html);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        String toString = attributes.toString();
        Assert.assertEquals("<attributes/>", toString);

        attributes.put("key", "value");
        toString = attributes.toString();
        Assert.assertEquals("<attributes><key>value</key></attributes>", toString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Assert.assertNotEquals(attributes1, attributes2);

        attributes1.put("key", "value1");
        attributes2.put("key", "value1");
        Assert.assertEquals(attributes1, attributes2);

        attributes2.put("key2", "value2");
        Assert.assertNotEquals(attributes1, attributes2);

        attributes1.put("key2", "value2");
        Assert.assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Assert.assertNotEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("key", "value1");
        attributes2.put("key", "value1");
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes2.put("key2", "value2");
        Assert.assertNotEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("key2", "value2");
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }
}