package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals("", attributes.get(""));
        Assert.assertEquals("", attributes.getIgnoreCase(""));
    }

    @Test
    public void testGetSetAndRemoveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));

        attributes.remove("key");
        Assert.assertEquals("", attributes.get("key"));
        Assert.assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetAndSetIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("value", attributes.get("key"));

        attributes.remove("key");
        attributes.putIgnoreCase("KEY", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("Value", attributes.getIgnoreCase("KEY"));
        Assert.assertEquals("", attributes.get("key"));
        Assert.assertEquals("", attributes.get("KEY"));
    }

    @Test
    public void testHasKeyAndHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey(""));
        Assert.assertFalse(attributes.hasKeyIgnoreCase(""));

        attributes.put("key", "Value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));

        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        attributes1.addAll(attributes2);

        List<String> keys = Arrays.asList("key1", "key2", "key3", "key4");
        for (String key : keys) {
            Assert.assertTrue(attributes1.hasKey(key));
            Assert.assertTrue(attributes1.hasKeyIgnoreCase(key));
            Assert.assertEquals(attributes2.get(key), attributes1.get(key));
            Assert.assertEquals(attributes2.getIgnoreCase(key), attributes1.getIgnoreCase(key));
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());

        List<String> keys = Arrays.asList("data-key1", "data-key2");
        for (String key : keys) {
            Assert.assertTrue(attributes.hasKey(key));
            Assert.assertTrue(attributes.hasKeyIgnoreCase(key));
            Assert.assertEquals(attributes.get(key), dataset.get(key));
            Assert.assertEquals(attributes.getIgnoreCase(key), dataset.get(key));
        }
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\" ");

        StringBuilder actual = new StringBuilder();
        attributes.html(actual, null);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes cloned = attributes.clone();
        Assert.assertEquals(2, cloned.size());

        List<String> keys = Arrays.asList("key1", "key2");
        for (String key : keys) {
            Assert.assertTrue(cloned.hasKey(key));
            Assert.assertTrue(cloned.hasKeyIgnoreCase(key));
            Assert.assertEquals(attributes.get(key), cloned.get(key));
            Assert.assertEquals(attributes.getIgnoreCase(key), cloned.getIgnoreCase(key));
        }
    }
}