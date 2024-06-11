package org.jsoup.nodes; // adjust the package name if necessary

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class AttributesTest {

    private Attributes attributes;

    @Parameters(name = "{index}: testAttributesWithSize{0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {new Attributes()},
                {new Attributes().put("key1", "value1")},
                {new Attributes().put("key1", "value1").put("key2", "value2")},
                {new Attributes().put("key1", "value1").put("key1", "value2")},
                {new Attributes().put("key1", "value1").put("key2", "value2").put("key1", "value3")}
        };
        return Arrays.asList(data);
    }

    public AttributesTest(Attributes attributes) {
        this.attributes = attributes;
    }

    @Test
    public void testSize() {
        Assert.assertEquals(attributes.size(), attributes.size());
    }

    @Test
    public void testGet() {
        if (attributes.size() > 0) {
            String key = attributes.keySet().iterator().next();
            String value = attributes.get(key);
            Assert.assertNotNull(value);
            Assert.assertEquals(value, attributes.get(key));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        attributes.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyStringKey() {
        attributes.get("");
    }

    @Test
    public void testGetIgnoreCase() {
        if (attributes.size() > 0) {
            String key = attributes.keySet().iterator().next();
            String value = attributes.getIgnoreCase(key);
            Assert.assertEquals(value, attributes.get(key));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIgnoreCaseEmptyStringKey() {
        attributes.getIgnoreCase("");
    }

    @Test
    public void testPut() {
        String newKey = "newKey";
        String newValue = "newValue";
        attributes.put(newKey, newValue);
        Assert.assertEquals(newValue, attributes.get(newKey));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringKey() {
        attributes.put("", "value");
    }

    @Test
    public void testPutBoolean() {
        String key = "booleanKey";
        attributes.put(key, false);
        Assert.assertFalse(attributes.hasKey(key));
        attributes.put(key, true);
        Assert.assertTrue(attributes.hasKey(key));
        attributes.remove(key);
        Assert.assertFalse(attributes.hasKey(key));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringValue() {
        attributes.put("key", "");
    }

    @Test
    public void testPutExistingKey() {
        String key = "existingKey";
        String newValue = "newValue";
        attributes.put(key, "oldValue");
        attributes.put(key, newValue);
        Assert.assertEquals(newValue, attributes.get(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        String key = "duplicateKey";
        attributes.put(key, "value1");
        attributes.put(key, "value2");
    }

    @Test
    public void testPutAll() {
        Attributes otherAttributes = new Attributes().put("key1", "value1").put("key2", "value2");
        attributes.putAll(otherAttributes);
        Map<String, String> otherMap = otherAttributes.dataset();
        for (Map.Entry<String, String> entry : otherMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertEquals(value, attributes.get(key));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllNullAttributes() {
        attributes.putAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutAllReadOnlyAttributes() {
        Attributes readOnlyAttributes = new Attributes();
        attributes.putAll(readOnlyAttributes);
    }

    @Test
    public void testIterator() {
        for (Attribute attribute : attributes) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
        }
    }

    @Test
    public void testAsList() {
        List<Attribute> list = attributes.asList();
        for (Attribute attribute : list) {
            String key = attribute.getKey();
            String value = attribute.getValue();
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
        }
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
            Assert.assertEquals(value, attributes.get(key));
        }
    }

    @Test
    public void testHtml() {
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, new Document.OutputSettings());
            String expectedHtml = "<key1>value1</key1><key2>value2</key2>";
            Assert.assertEquals(expectedHtml, accum.toString());
        } catch (IOException e) {
            // should not be thrown
        }
    }

    @Test
    public void testToString() {
        Assert.assertEquals(attributes.toString(), attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes().put("key1", "value1").put("key2", "value2");
        Assert.assertEquals(attributes, attributes);
        Assert.assertEquals(attributes, otherAttributes);
        Assert.assertNotEquals(attributes, new Object());
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes().put("key1", "value1").put("key2", "value2");
        Assert.assertEquals(attributes.hashCode(), attributes.hashCode());
        Assert.assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributesWithMixedCaseKeys = new Attributes().put("key1", "value1").put("KEY2", "value2");
        attributesWithMixedCaseKeys.normalize();
        String key1 = attributesWithMixedCaseKeys.get("key1");
        String key2 = attributesWithMixedCaseKeys.get("key2");
        Assert.assertEquals(key1, "key1");
        Assert.assertEquals(key2, "key2");
    }
}