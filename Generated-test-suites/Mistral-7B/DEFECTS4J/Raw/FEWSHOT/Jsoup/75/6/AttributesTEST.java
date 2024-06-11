package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        assertEquals(value1, attributes.get(key1));

        attributes.put(key2, value2);
        assertEquals(value2, attributes.get(key2));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "Key2";
        String value2 = "value2";

        attributes.putIgnoreCase(key1, value1);
        assertEquals(value1, attributes.get(key1));

        attributes.putIgnoreCase(key2, value2);
        assertEquals(value2, attributes.get(key2));
    }

    @Test
    public void testPutReplace() {
        Attributes attributes = new Attributes();
        String key = "key";
        String oldValue = "oldValue";
        String newValue = "newValue";

        attributes.put(key, oldValue);
        assertEquals(oldValue, attributes.get(key));

        attributes.put(key, newValue);
        assertEquals(newValue, attributes.get(key));
        assertNull(attributes.get(key), "Old value should be replaced");
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        boolean value = true;

        attributes.put(key, value);
        assertTrue(attributes.get(key) == null);

        attributes.put(key, false);
        assertFalse(attributes.hasKey(key));
        assertNull(attributes.get(key));
        attributes.remove(key);

        attributes.put(key, value);
        assertTrue(attributes.hasKey(key));
        assertTrue(attributes.get(key) == null);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");

        attributes.put(attribute);
        assertEquals("value", attributes.get(attribute.getKey()));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        attributes.remove(key1);
        assertFalse(attributes.hasKey(key1));
        assertNull(attributes.get(key1));

        attributes.remove(key2);
        assertFalse(attributes.hasKey(key2));
        assertNull(attributes.get(key2));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "Key2";
        String value2 = "value2";

        attributes.putIgnoreCase(key1, value1);
        attributes.putIgnoreCase(key2, value2);

        attributes.removeIgnoreCase(key1);
        assertFalse(attributes.hasKey(key1));
        assertNull(attributes.get(key1));

        attributes.removeIgnoreCase(key2);
        assertFalse(attributes.hasKey(key2));
        assertNull(attributes.get(key2));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String key2 = "key2";

        assertFalse(attributes.hasKey(key1));
        assertFalse(attributes.hasKey(key2));

        attributes.put(key1, "value1");
        assertTrue(attributes.hasKey(key1));
        assertFalse(attributes.hasKey(key2));

        attributes.put(key2, "value2");
        assertTrue(attributes.hasKey(key1));
        assertTrue(attributes.hasKey(key2));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String key2 = "Key2";

        assertFalse(attributes.hasKeyIgnoreCase(key1));
        assertFalse(attributes.hasKeyIgnoreCase(key2));

        attributes.putIgnoreCase(key1, "value1");
        assertTrue(attributes.hasKeyIgnoreCase(key1));
        assertFalse(attributes.hasKeyIgnoreCase(key2));

        attributes.putIgnoreCase(key2, "value2");
        assertTrue(attributes.hasKeyIgnoreCase(key1));
        assertTrue(attributes.hasKeyIgnoreCase(key2));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleImmutableEntry<>(key1, value1),
                new AbstractMap.SimpleImmutableEntry<>(key2, value2)
        );

        for (Map.Entry<String, String> entry : attributes) {
            assertEquals(entry.getKey(), expected.get(0).getKey());
            assertEquals(entry.getValue(), expected.get(0).getValue());
            expected.remove(0);
        }

        assertTrue(expected.isEmpty());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        List<Attribute> attributesList = attributes.asList();

        assertEquals(attributesList.size(), 2);
        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getKey(), key1
                    ? (attributes.get(key1) == null ? "" : attributes.get(key1))
                    : key2);
            assertEquals(attribute.getValue(), value1
                    ? (attributes.get(key1) == null ? "" : attributes.get(key1))
                    : value2);
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        String dataKey1 = "data-key1";
        String dataValue1 = "data-value1";
        String dataKey2 = "data-key2";
        String dataValue2 = "data-value2";

        attributes.put(dataKey1, dataValue1);
        attributes.put(dataKey2, dataValue2);

        Map<String, String> dataset = attributes.dataset();

        assertEquals(dataset.size(), 2);
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            assertEquals(entry.getKey(), dataKey1
                    ? (attributes.get(dataKey1) == null ? "" : attributes.get(dataKey1))
                    : dataKey2);
            assertEquals(entry.getValue(), dataValue1
                    ? (attributes.get(dataKey1) == null ? "" : attributes.get(dataKey1))
                    : dataValue2);
        }

        assertEquals(attributes.size(), 2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        Attributes cloned = attributes.clone();

        assertEquals(cloned.size(), attributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            String key = attributes.getKeyAt(i);
            String value = attributes.getValueAt(i);
            String clonedKey = cloned.getKeyAt(i);
            String clonedValue = cloned.getValueAt(i);

            assertEquals(key, clonedKey);
            assertEquals(value, clonedValue);
        }
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "KEY2";
        String value2 = "value2";

        attributes.put(key1, value1);
        attributes.put(key2, value2);

        attributes.normalize();

        for (int i = 0; i < attributes.size(); i++) {
            String key = attributes.getKeyAt(i);
            String value = attributes.getValueAt(i);

            assertEquals(key.toLowerCase(), key1
                    ? key1
                    : key2);
        }
    }
}