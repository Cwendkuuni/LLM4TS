package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testPutGet() {
        Attributes attributes = new Attributes();

        String value1 = "value1";
        String value2 = "value2";

        attributes.put("key1", value1);
        assertEquals(value1, attributes.get("key1"));

        attributes.put("key1", value2);
        assertEquals(value2, attributes.get("key1"));

        assertEquals(value1, attributes.get("key1", "defaultValue"));

        assertNull(attributes.get("key2"));
        assertEquals("", attributes.get("key2"));

        attributes.put("key2", value2);
        assertEquals(value2, attributes.get("key2"));
    }

    @Test
    public void testPutNull() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("key1"));

        attributes.put("key1", null);
        assertNull(attributes.get("key1"));

        attributes.put(null, null);
        assertFalse(attributes.isEmpty());
    }

    @Test
    public void testPutFalseValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", false);
        assertTrue(attributes.isEmpty());
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key1", "false"));
        assertFalse(attributes.hasKey("key1", false));
        assertNull(attributes.get("key1", "defaultValue"));
        assertNull(attributes.get("key1", false));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        assertEquals(attribute, attributes.get("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertThrows(IllegalArgumentException.class, () -> attributes.put("key1", "value2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertNull(attributes.getIgnoreCase("key1"));
        attributes.put("KEY1", "value2");
        assertEquals("value2", attributes.getIgnoreCase("KEY1"));
        attributes.removeIgnoreCase("KEY1");
        assertNull(attributes.getIgnoreCase("KEY1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("", ""));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value1", clonedAttributes.get("key1"));
        assertFalse(Objects.equals(attributes, clonedAttributes));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals("value1", attributes3.get("key1"));
        assertEquals
                ("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
        assertEquals(3, attributes3.size());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("key1=\"value1\"", attributes.toString());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("key1"));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertFalse(attributes1.equals(attributes2));

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));

        attributes2.put("key2", "value2");
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());

        attributes2.put("key2", "value2");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        ));

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        assertEquals(list.get(0).getKey(), iterator.next().getKey());
        assertEquals(list.get(0).getValue(), iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }
}