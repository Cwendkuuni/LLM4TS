package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testCheckCapacity() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(16);
        assertSame(attributes.keys, new String[16]);
        assertSame(attributes.vals, new String[16]);
    }

    @Test
    public void testCopyOf() {
        String[] orig = {"key1", "key2"};
        String[] copy = Attributes.copyOf(orig, 5);
        assertArrayEquals(orig, copy);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyOfNullOrig() {
        Attributes.copyOf(null, 5);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCopyOfNegativeSize() {
        String[] orig = {"key1", "key2"};
        Attributes.copyOf(orig, -1);
    }

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertArrayEquals(new String[0], attributes.keys);
        assertArrayEquals(new String[0], attributes.vals);
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals(EmptyString, attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        assertEquals("Value1", attributes.getIgnoreCase("key1"));
        assertEquals("Value1", attributes.getIgnoreCase("Key1"));
        assertEquals(EmptyString, attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        assertEquals(EmptyString, attributes.get("key2"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals(EmptyString, attributes.get("key2"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "Value1");
        assertEquals("Value1", attributes.getIgnoreCase("key1"));
        assertEquals("Value1", attributes.getIgnoreCase("Key1"));
        assertEquals(EmptyString, attributes.getIgnoreCase("key2"));

        attributes.putIgnoreCase("key1", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("Key1"));
        assertEquals(EmptyString, attributes.getIgnoreCase("key2"));

        attributes.putIgnoreCase("key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("Key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBooleanWithValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals("true", attributes.get("key1"));
    }

    @Test
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
        assertSame(attributes, attributes.put("key1", null));
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes updatedAttributes = attributes.put("key1", "value2");
        assertEquals("value2", updatedAttributes.get("key1"));
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put("", "value");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutWithExistingKeyAndNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", null);
        assertNull(attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutWithExistingKeyAndValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutWithNewKeyAndValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.remove(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testRemoveWithNewKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key2");
        assertFalse(attributes.hasKey("key2"));
        assertNull(attributes.get("key2"));
    }

    @Test
    public void testRemoveWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveWithExistingKeyAndValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertNull(attributes.get("key1"));
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyWithNullKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(null));
    }

    @Test
    public void testHasKeyWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(""));
    }

    @Test
    public void testHasKeyWithExistingKeyWithDifferentCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testAddAllWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(new Attributes());
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAllWithSelf() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.addAll(attributes);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        assertEquals(EmptyString, attributes.toString());
        attributes.put("key1", "value1");
        assertEquals("key1=\"value1\"", attributes.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = attributes1.clone();
        assertEquals(1, attributes2.size());
        assertTrue(attributes2.hasKey("key1"));
        assertEquals("value1", attributes2.get("key1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
        attributes.put("data-key1", "data-value1");
        assertEquals(1, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertEquals("data-value1", dataset.get("data-key1"));
    }

    @Test
    public void testDatasetWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            list.add(entry);
        }
        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "VALUE1");
        attributes.normalize();
        assertEquals("key1", attributes.get("key1"));
        assertEquals("value1", attributes.get("key1"));
    }
}