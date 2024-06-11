package org.test; // replace with your test package if needed

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {
    // Basic functionality tests

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);

        Assert.assertFalse(attributes.get("key").isEmpty());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);

        attributes.put(attribute);

        String result = attributes.get(key);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");

        String result = attributes.get("key");
        Assert.assertEquals(Attributes.EMPTY_STRING, result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");

        String result = attributes.get("Key");
        Assert.assertEquals(Attributes.EMPTY_STRING, result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        int capacity = -1;
        Attributes attributes = new Attributes(capacity);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        Attributes emptyAttributes = new Attributes();

        attributes.addAll(emptyAttributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllWithNullAttributes() {
        Attributes attributes = new Attributes();
        Attributes nullAttributes = null;

        attributes.addAll(nullAttributes);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBounds() {
        Attributes attributes = new Attributes();
        attributes.remove(1);
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String result1 = attributes.get("key");
        Assert.assertEquals("value2", result1);

        String result2 = attributes.get("key");
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testPutDuplicateKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("key", "value2");

        String result1 = attributes.get("Key");
        Assert.assertEquals("value2", result1);

        String result2 = attributes.get("key");
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testPutIgnoreCaseWithSameCaseKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.put("Key", "value2");

        String result1 = attributes.get("Key");
        Assert.assertEquals("value2", result1);

        String result2 = attributes.get("Key");
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testPutWithDataPrefix() {
        Attributes attributes = new Attributes();
        String key = "data-key";
        String value = "value";

        attributes.put(key, value);
        String dataKey = dataPrefix + key;

        String result = attributes.get(key);
        Assert.assertEquals(value, result);

        String resultData = attributes.getIgnoreCase(dataKey);
        Assert.assertEquals(value, resultData);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithNullOutputSettings() {
        Attributes attributes = new Attributes();
        StringBuilder sb = StringUtil.borrowBuilder();

        try {
            attributes.html(sb, null);
        } catch (IOException e) {
            throw new SerializationException(e);
        } finally {
            StringUtil.releaseBuilder(sb);
        }
    }

    @Test(expected = IOException.class)
    public void testHtmlWithInvalidOutputSettings() throws IOException {
        Attributes attributes = new Attributes();
        StringBuilder sb = StringUtil.borrowBuilder();

        try {
            Document.OutputSettings outputSettings = new Document.OutputSettings();
            outputSettings.setAutoCollapseTags(true);
            outputSettings.setPrettify(false);
            attributes.html(sb, outputSettings);
        } finally {
            StringUtil.releaseBuilder(sb);
        }
    }
}