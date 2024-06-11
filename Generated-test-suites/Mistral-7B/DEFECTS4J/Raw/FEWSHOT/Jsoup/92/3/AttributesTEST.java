package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributesWithEmptyValues() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "");
        attributes.put("key2", "");

        assertEquals(2, attributes.size());
        assertEquals("", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));

        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testAttributesWithSingleValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));

        assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithMultipleValues() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1")); // last value wins
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));

        assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1")); // boolean values are stored as keys
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithDuplicateBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key1", true);

        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1")); // boolean values are stored as keys
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);

        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1")); // null values are stored as keys
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithDuplicateNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
        attributes.put("key1", null);

        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1")); // null values are stored as keys
        assertTrue(attributes.hasKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testAttributesWithDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("data-key1"));
        assertTrue(attributes.hasKey("data-key1"));
        assertTrue(attributes.hasKeyIgnoreCase("data-key1"));

        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertTrue(dataset.containsKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(dataset.containsKey("key2"));
    }

    @Test
    public void testAttributesWithMultipleDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key1", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("data-key1")); // last value wins
        assertTrue(attributes.hasKey("data-key1"));
        assertTrue(attributes.hasKeyIgnoreCase("data-key1"));

        assertEquals(1, dataset.size());
        assertEquals("value2", dataset.get("key1"));
        assertTrue(dataset.containsKey("key1"));

        assertFalse(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(dataset.containsKey("key2"));
    }

    @Test
    public void testAttributesWithCaseInsensitiveValues() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "VALUE1");

        assertEquals(1, attributes.size());
        assertEquals("VALUE1", attributes.getIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));

        assertNull(attributes.get("key2"));
    }

    @Test
    public void testAttributesWithMultipleCaseInsensitiveValues() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "VALUE1");
        attributes.put("key1", "VALUE2");

        assertEquals(1, attributes.size());
        assertEquals("VALUE2", attributes.getIgnoreCase("key1")); // last value wins
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));

        assertNull(attributes.get("key2"));
    }

    @Test
    public void testAttributesWithRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");

        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        assertNull(attributes.get("key1"));
        assertNull(attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testAttributesWithRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("key1");

        assertEquals(0, attributes.size());
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        assertNull(attributes.get("key1"));
        assertNull(attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testAttributesWithPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("key1", "value2");

        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertNull(attributes.get("key2"));
    }

    @Test
    public void testAttributesWithPutMethodAndPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        attributes.putIgnoreCase("key1", "value3");

        assertEquals(1, attributes.size());
        assertEquals("value3", attributes.get("key1"));
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        assertNull(attributes.get("key2"));
    }

    @Test
    public void testAttributesWithPutAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes1.putAll(attributes2);

        assertEquals(3, attributes1.size());
        assertEquals("value2", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));

        assertFalse(attributes1.hasKeyIgnoreCase("key4"));
    }

    @Test
    public void testAttributesWithIterableMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = Arrays.asList(attributes.iterator().toArray(new Attribute[0]));

        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("key2", list.get(1).getKey());
    }

    @Test
    public void testAttributesWithCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(1, attributes.size());
        assertEquals(1, clonedAttributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", clonedAttributes.get("key1"));
        assertSame(attributes, clonedAttributes.parent);
    }
}