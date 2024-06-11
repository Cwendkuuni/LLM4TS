package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testInitialState() {
        Attributes attributes = new Attributes();
        assertEquals(Collections.emptyMap(), attributes.asMap());
        assertEquals(0, attributes.size());
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testBasicPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testPutOverwrites() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutOverwritesCaseInsensitive() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("KEY", "value2");

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key1", "value1");
        attributes.putIgnoreCase("KEY2", "value2");

        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("KEY2"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveCaseInsensitive() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");

        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
        assertEquals("value2", attributes.get("KEY2"));
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();

        attributes.remove("nonExistentKey");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);

        assertEquals(1, attributes.size());
        assertTrue(attributes.get("booleanKey").equals(""));
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutBooleanAttributeTwice() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        attributes.put("booleanKey", false);

        assertEquals(1, attributes.size());
        assertTrue(attributes.get("booleanKey").equals(""));
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutBooleanAttributeValueIsFalse() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", false);

        assertEquals(1, attributes.size());
        assertNull(attributes.get("booleanKey"));
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutBooleanAttributeValueIsNull() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", null);

        assertEquals(1, attributes.size());
        assertSame(attributes, attributes.get("booleanKey"));
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutDuplicateBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        attributes.put("booleanKey", true);

        assertEquals(1, attributes.size());
        assertTrue(attributes.get("booleanKey").equals("true"));
        assertTrue(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAndGetAttributes() {
        Attributes attributes = new Attributes();

        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("attr1", "value1"),
            new AbstractMap.SimpleImmutableEntry<>("attr2", "value2")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithEmptyValues() {
        Attributes attributes = new Attributes();

        attributes.put("attr1", "");
        attributes.put("attr2", "");

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("attr1", ""),
            new AbstractMap.SimpleImmutableEntry<>("attr2", "")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithNullValues() {
        Attributes attributes = new Attributes();

        attributes.put("attr1", null);
        attributes.put("attr2", null);

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("attr1", null),
            new AbstractMap.SimpleImmutableEntry<>("attr2", null)
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithDuplicateKeys() {
        Attributes attributes = new Attributes();

        attributes.put("duplicateKey1", "value1");
        attributes.put("duplicateKey1", "value2");

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("duplicateKey1", "value2")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithDuplicateKeysIgnoreCase() {
        Attributes attributes = new Attributes();

        attributes.put("DuplicateKey1", "value1");
        attributes.put("DuplicateKey1", "value2");

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("DuplicateKey1", "value2")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithSingleKey() {
        Attributes attributes = new Attributes();
        attributes.put("singleKey", "singleValue");

        List<Map.Entry<String, String>> expected = Collections.singletonList(
            new AbstractMap.SimpleImmutableEntry<>("singleKey", "singleValue")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithEmptyMap() {
        Attributes attributes = new Attributes();
        Attributes otherAttributes = new Attributes();

        otherAttributes.put("otherKey1", "otherValue1");
        otherAttributes.put("otherKey2", "otherValue2");

        attributes.putAll(otherAttributes);

        List<Map.Entry<String, String>> expected = Arrays.asList(
            new AbstractMap.SimpleImmutableEntry<>("otherKey1", "otherValue1"),
            new AbstractMap.SimpleImmutableEntry<>("otherKey2", "otherValue2")
        );

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testPutAndGetAttributesWithNullMap() {
        Attributes attributes = new Attributes();

        attributes.putAll(null);

        assertEquals(Collections.emptyList(), attributes.asList());
    }

    @Test(expected = NullPointerException.class)
    public void testPutAndGetAttributesWithNullValuesList() {
        Attributes attributes = new Attributes();

        attributes.putAll((List<Map.Entry<String, String>>) null);
    }

    @Test
    public void testPutAndGetAttributesWithLargeMap() {
        Attributes attributes = new Attributes();

        List<Map.Entry<String, String>> largeMap = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeMap.add(new AbstractMap.SimpleImmutableEntry<>("key" + i, "value" + i));
        }

        attributes.putAll(largeMap);

        List<Map.Entry<String, String>> expected = largeMap;

        assertEquals(expected, attributes.asList());
    }

    @Test
    public void testContainsKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.containsKey("key1"));
        assertTrue(attributes.containsKey("key2"));
        assertFalse(attributes.containsKey("nonExistentKey"));
    }

    @Test
    public void testContainsKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        assertTrue(attributes.containsKeyIgnoreCase("KEY1"));
        assertTrue(attributes.containsKeyIgnoreCase("KEY2"));
        assertFalse(attributes.containsKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");

        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY2"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testIsEmpty() {
        Attributes attributes = new Attributes();

        assertTrue(attributes.isEmpty());

        attributes.put("key1", "value1");

        assertFalse(attributes.isEmpty());
    }
}