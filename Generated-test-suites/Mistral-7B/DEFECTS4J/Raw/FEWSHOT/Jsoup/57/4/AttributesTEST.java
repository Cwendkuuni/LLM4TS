package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(Collections.emptyMap(), attributes.asMap());
        assertEquals(Collections.emptyList(), attributes.asList());
        assertTrue(attributes.size() == 0);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertFalse(attributes.hasKey("otherKey"));
        assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
        assertNull(attributes.get("otherKey"));
        assertNull(attributes.getIgnoreCase("otherKey"));
    }

    @Test
    public void testPutOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", "oldValue");
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
        assertEquals("newValue", attributes.getIgnoreCase("key"));
        assertFalse(attributes.hasKey("keyOther"));
        assertFalse(attributes.hasKeyIgnoreCase("keyOther"));
        assertNull(attributes.get("keyOther"));
        assertNull(attributes.getIgnoreCase("keyOther"));
    }

    @Test
    public void testPutReplace() {
        Attributes attributes = new Attributes();
        attributes.put("key", new BooleanAttribute("key"));
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertFalse(attributes.hasKey("keyBoolean"));
        assertFalse(attributes.hasKeyIgnoreCase("keyBoolean"));
        assertNull(attributes.get("keyBoolean"));
        assertNull(attributes.getIgnoreCase("keyBoolean"));
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testPutDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
        assertFalse(attributes.hasKey("keyOther"));
        assertFalse(attributes.hasKeyIgnoreCase("keyOther"));
        assertNull(attributes.get("keyOther"));
        assertNull(attributes.getIgnoreCase("keyOther"));
    }

    @Test
    public void testPutNull() {
        Attributes attributes = new Attributes();
        assertThrows(NullPointerException.class, () -> attributes.put(null, "value"));
    }

    @Test
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        assertThrows(NullPointerException.class, () -> attributes.put(null, new BooleanAttribute("key")));
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutNullValueBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", new BooleanAttribute("key", false));
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals("true", attributes.get("key"));
        assertEquals("true", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanSame() {
        Attributes attributes = new Attributes();
        attributes.put("key", new BooleanAttribute("key", true));
        attributes.put("key", new BooleanAttribute("key", true));
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals("true", attributes.get("key"));
        assertEquals("true", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveNonExistent() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.remove("key"));
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveEmpty() {
        Attributes attributes = new Attributes();
        attributes.remove("key");
        assertEquals(Collections.emptyMap(), attributes.asMap());
        assertEquals(Collections.emptyList(), attributes.asList());
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("otherKey"));
        assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }

    @Test
    public void testHasKeyEmpty() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testGetEmpty() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetNull() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetIgnoreCaseNull() {
        Attributes attributes = new Attributes();
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetIgnoreCaseNonexistent() {
        Attributes attributes = new Attributes();
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Map<String, String> map = attributes.asMap();
        List<Attribute> list = attributes.asList();

        attributes.clear();
        assertEquals(Collections.emptyMap(), attributes.asMap());
        assertEquals(Collections.emptyList(), attributes.asList());
        assertTrue(attributes.size() == 0);
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertNull(attributes.get("key1"));
        assertNull(attributes.getIgnoreCase("key1"));
        assertNull(attributes.get("key2"));
        assertNull(attributes.getIgnoreCase("key2"));

        assertEquals(map, attributes.asMap());
        assertEquals(list, attributes.asList());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        Map<String, String> map = attributes1.asMap();
        List<Attribute> list = attributes1.asList();
        Map<String, String> map2 = attributes2.asMap();
        List<Attribute> list2 = attributes2.asList();

        assertEquals(Arrays.asList(map, list), result.asMapAndList());
        assertEquals(Arrays.asList(map2, list2), result.addAll(Arrays.asList(attributes2, attributes1)).asMapAndList());
    }

    @Test
    public void testAddAllEmpty() {
        Attributes attributes1 = new Attributes();
        Attributes result = new Attributes();

        result.addAll(Collections.emptyList());
        assertEquals(Collections.emptyMap(), result.asMap());
        assertEquals(Collections.emptyList(), result.asList());
        assertTrue(result.size() == 0);
    }

    @Test
    public void testAddAllSelf() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.addAll(attributes);
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAllDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value3");

        Attributes result = new Attributes();
        result.addAll(attributes);
        result.addAll(Arrays.asList(attributes2));

        assertEquals("value2", result.get("key"));
        assertEquals("value2", result.getIgnoreCase("key"));
        assertTrue(result.hasKey("key"));
        assertTrue(result.hasKeyIgnoreCase("key"));
        assertTrue(result.size() == 1);
    }

    @Test
    public void testAddAllEmptySelf() {
        Attributes attributes = new Attributes();
        attributes.addAll(attributes);
        assertEquals(Collections.emptyMap(), attributes.asMap());
        assertEquals(Collections.emptyList(), attributes.asList());
        assertTrue(attributes.size() == 0);
    }

    @Test
    public void testDatasetEmpty() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(Collections.emptyMap(), dataset);
    }

    @Test
    public void testDatasetNonEmpty() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(Arrays.asList("data-key1", "data-key2"), dataset.keySet());
        assertEquals(Arrays.asList("value1", "value2"), dataset.values());
    }

    @Test
    public void testDatasetOverwrite() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key1", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value2", dataset.get("data-key1"));
        assertEquals(Arrays.asList("data-key1"), dataset.keySet());
        assertEquals(Collections.singletonMap("data-key1", "value2"), dataset);
    }

    @Test
    public void testDatasetDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key1", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value2", dataset.get("data-key1"));
        assertEquals(Arrays.asList("data-key1"), dataset.keySet());
        assertEquals(Collections.singletonMap("data-key1", "value2"), dataset);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes cloned = attributes.clone();

        assertEquals("value", cloned.get("key"));
        assertEquals("value", cloned.getIgnoreCase("key"));
        assertTrue(cloned.hasKey("key"));
        assertTrue(cloned.hasKeyIgnoreCase("key"));
        assertEquals(1, cloned.size());
    }

    @Test
    public void testCloneEmpty() {
        Attributes attributes = new Attributes();
        Attributes cloned = attributes.clone();
        assertEquals(Collections.emptyMap(), cloned.asMap());
        assertEquals(Collections.emptyList(), cloned.asList());
        assertTrue(cloned.size() == 0);
    }
}