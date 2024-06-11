package org.jsoup.nodes; // update the package name if necessary

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {
    

    // Basic test cases for each method

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.get("key").equals("true"));
        assertNull(attributes.get("key").trim());
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        String result = attributes.get("key1");
        String expected = "value2";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithExistingIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        attributes.putIgnoreCase("Key1", "value2");
        String result = attributes.getIgnoreCase("Key1");
        String expected = "value2";
        assertEquals(expected, result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyWithNoKey() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCaseWithNoKey() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertFalse(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        int expected = 2;
        assertEquals(expected, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key3", "value3");
        attributes1.addAll(attributes2);
        int sizeBeforeAddAll = attributes1.size();
        String[] keysBeforeAddAll = attributes1.keys;

        int expectedSize = sizeBeforeAddAll + attributes2.size();
        String[] expectedKeys = new String[expectedSize];
        System.arraycopy(keysBeforeAddAll, 0, expectedKeys, 0, sizeBeforeAddAll.length);
        System.arraycopy(attributes2.keys, 0, expectedKeys, sizeBeforeAddAll.length, attributes2.size.length);

        assertEquals(expectedSize, attributes1.size());
        assertArrayEquals(expectedKeys, attributes1.keys);

        for (String key : attributes2.keys) {
            assertTrue(attributes1.hasKey(key));
            String value = attributes1.get(key);
            assertEquals(attributes2.get(key), value);
        }
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Map.Entry<String, String>> iterator = attributes.asList().iterator();
        Map.Entry<String, String> entry = iterator.next();
        String key = entry.getKey();
        String value = entry.getValue();

        assertEquals("key1", key);
        assertEquals("value1", value);

        iterator.hasNext();
        entry = iterator.next();

        assertEquals("key2", key);
        assertEquals("value2", value);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        Map.Entry<String, String> entry = dataset.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();

        assertEquals("data-key", key);
        assertEquals("value", value);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder expected = new StringBuilder();
        attributes.html(expected, null);

        String result = attributes.html();
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        attributes2.put("key2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            String key = attributes.keys[i];
            String value = attributes.vals[i];
            String clonedKey = clonedAttributes.keys[i];
            String clonedValue = clonedAttributes.vals[i];

            assertEquals(key, clonedKey);
            assertEquals(value, clonedValue);
        }
    }

    // Edge cases and exception handling test cases

    @Test
    public void testGetNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.get(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        String result = attributes.get("key");
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, "value");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        String result = attributes.get("key");
        assertNull(result);
    }

    @Test
    public void testPutWithExistingKeyAndNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", null);
        String result = attributes.get("key");
        assertNull(result);
    }

    @Test
    public void testPutWithExistingIgnoreCaseKeyAndNullValue() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value1");
        attributes.putIgnoreCase("key", null);
        String result = attributes.getIgnoreCase("key");
        assertNull(result);
    }

    @Test
    public void testPutWithExistingKeyAndSameValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "value");
        String result = attributes.get("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithExistingIgnoreCaseKeyAndSameValue() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        attributes.putIgnoreCase("key", "value");
        String result = attributes.getIgnoreCase("key");
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithLongKey() {
        Attributes attributes = new Attributes();
        String longKey = "key";
        for (int i = 0; i < 255; i++) {
            longKey += "0";
        }
        attributes.put(longKey, "value");
        String result = attributes.get(longKey);
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithLongKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        String longKey = "key";
        for (int i = 0; i < 255; i++) {
            longKey += "0";
        }
        attributes.putIgnoreCase(longKey, "value");
        String result = attributes.getIgnoreCase(longKey);
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put("", "value");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutWithEmptyKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        try {
            attributes.putIgnoreCase("", "value");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutWithSpaceKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(" key ", "value");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutWithSpaceKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        try {
            attributes.putIgnoreCase(" key ", "value");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutWithKeyStartingWithDataPrefix() {
        Attributes attributes = new Attributes();
        String dataKey = dataPrefix + "key";
        attributes.put(dataKey, "value");
        String result = attributes.get(dataKey);
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test
    public void testPutWithKeyStartingWithDataPrefixIgnoreCase() {
        Attributes attributes = new Attributes();
        String dataKey = dataPrefix + "Key";
        attributes.putIgnoreCase(dataKey, "value");
        String result = attributes.getIgnoreCase(dataKey);
        String expected = "value";
        assertEquals(expected, result);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithNullAccumulator() throws IOException {
        Attributes attributes = new Attributes();
        attributes.html(null, null);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithNullOutputSettings() throws IOException {
        Attributes attributes = new Attributes();
        StringBuilder accumulator = new StringBuilder();
        attributes.html(accumulator, null);
    }

    @Test
    public void testCloneWithNullParent() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        Attributes clonedAttributes = attributes.clone();
        clonedAttributes.parent = null;
        Attributes originalAttributes = attributes;
        Attributes clonedOriginalAttributes = (Attributes) clonedAttributes.clone();

        assertEquals(originalAttributes.size(), clonedAttributes.size());
        assertEquals(originalAttributes.size(), clonedOriginalAttributes.size());

        for (int i = 0; i < originalAttributes.size(); i++) {
            String key = originalAttributes.keys[i];
            String value = originalAttributes.vals[i];
            String clonedKey = clonedAttributes.keys[i];
            String clonedValue = clonedAttributes.vals[i];
            String clonedOriginalKey = clonedOriginalAttributes.keys[i];
            String clonedOriginalValue = clonedOriginalAttributes.vals[i];

            assertEquals(key, clonedKey);
            assertEquals(value, clonedValue);
            assertEquals(key, clonedOriginalKey);
            assertEquals(value, clonedOriginalValue);
        }
    }

    
}