package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;
    private Attributes otherAttributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        otherAttributes = new Attributes();
    }

    @Test
    public void testGetEmpty() {
        assertEquals("", attributes.get(""));
        assertEquals("", attributes.getIgnoreCase(""));
        assertNull(attributes.get("Key"));
        assertNull(attributes.getIgnoreCase("KEY"));
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKeyIgnoreCase(""));
        assertFalse(attributes.hasKey("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testPutGet() {
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.get("Key"));
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertTrue(attributes.hasKey("Key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));

        attributes.put("OtherKey", "OtherValue");
        assertEquals("OtherValue", attributes.get("OtherKey"));
        assertEquals("OtherValue", attributes.getIgnoreCase("otherkey"));
        assertTrue(attributes.hasKey("OtherKey"));
        assertTrue(attributes.hasKeyIgnoreCase("otherkey"));

        assertEquals(2, attributes.size());
    }

    @Test
    public void testPutReplace() {
        attributes.put("Key", "OldValue");
        attributes.put("Key", "NewValue");
        assertEquals("NewValue", attributes.get("Key"));
        assertEquals("NewValue", attributes.getIgnoreCase("key"));
        assertTrue(attributes.hasKey("Key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("OldKey"));
        assertFalse(attributes.hasKeyIgnoreCase("oldkey"));

        attributes.put("Key", null);
        assertNull(attributes.get("Key"));
        assertNull(attributes.getIgnoreCase("key"));
        assertFalse(attributes.hasKey("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("OtherKey"));
        assertFalse(attributes.hasKeyIgnoreCase("otherkey"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("Key", false);
        assertFalse(attributes.get("Key").isSet());
        assertFalse(attributes.hasKey("Key"));

        attributes.put("Key", true);
        assertTrue(attributes.get("Key").isSet());
        assertTrue(attributes.hasKey("Key"));
    }

    @Test
    public void testPutSameKeyTwice() {
        attributes.put("Key", "Value1");
        attributes.put("Key", "Value2");
        assertEquals("Value2", attributes.get("Key"));
        assertEquals("Value2", attributes.getIgnoreCase("key"));
        assertTrue(attributes.hasKey("Key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        attributes.put("Key1", "Value1");
        assertEquals("Value1", attributes.get("Key1"));
        assertTrue(attributes.hasKey("Key1"));
        attributes.remove("Key1");
        assertNull(attributes.get("Key1"));
        assertFalse(attributes.hasKey("Key1"));

        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.remove("Key1");
        assertEquals("Value2", attributes.get("Key2"));
        assertFalse(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKey("Key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.removeIgnoreCase("key1");
        assertNull(attributes.get("Key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertSame("Value2", attributes.get("Key2"));
        assertTrue(attributes.hasKey("Key2"));
    }

    @Test
    public void testHasKey() {
        assertFalse(attributes.hasKey(""));
        assertFalse(attributes.hasKeyIgnoreCase(""));
        assertFalse(attributes.hasKey("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("Key1", "Value1");
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("Key2"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));

        attributes.put("Key2", "Value2");
        assertTrue(attributes.hasKey("Key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertTrue(attributes.hasKey("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());

        attributes.put("Key1", "Value1");
        assertEquals(1, attributes.size());

        attributes.put("Key2", "Value2");
        assertEquals(2, attributes.size());

        attributes.remove("Key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("Key3", "Value3");
        otherAttributes.put("Key4", "Value4");

        attributes.addAll(otherAttributes);
        assertEquals(4, attributes.size());
        assertTrue(attributes.hasKey("Key3"));
        assertTrue(attributes.hasKeyIgnoreCase("key3"));
        assertTrue(attributes.hasKey("Key4"));
        assertTrue(attributes.hasKeyIgnoreCase("key4"));
    }

    @Test
    public void testAsList() {
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("Value1", list.get(0).getValue());
        assertEquals("Value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("Key1", "Value1");
        attributes.put("data-Key2", "Value2");
        attributes.put("Key3", "Value3");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("Value2", dataset.get("Key2"));
        assertEquals("Value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() throws Exception {
        attributes.put("Key1", "Value1");
        StringBuilder sb = new StringBuilder();
        attributes.html(sb);
        assertEquals("Key1=\"Value1\"", sb.toString());
    }

    @Test
    public void testClone() {
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(2, clonedAttributes.size());
        assertEquals("Value1", clonedAttributes.get("Key1"));
        assertEquals("Value2", clonedAttributes.get("Key2"));

        attributes.remove("Key1");
        assertEquals(1, attributes.size());
        assertEquals(2, clonedAttributes.size());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = attributes;

        assertEquals(attributes, attributes);
        assertEquals(attributes, attributes3);
        assertEquals(attributes, attributes4);
        assertNotEquals(attributes, attributes1);
        assertNotEquals(attributes, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        Attributes attributes4 = attributes;

        assertEquals(attributes.hashCode(), attributes.hashCode());
        assertEquals(attributes.hashCode(), attributes3.hashCode());
        assertEquals(attributes.hashCode(), attributes4.hashCode());
        assertNotEquals(attributes.hashCode(), attributes1.hashCode());
        assertNotEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testDataKey() {
        assertEquals("data-Key", Attributes.dataKey("Key"));
        assertEquals("data-Key1", Attributes.dataKey("Key1"));
    }

    @Test
    public void testIterator() {
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(2, list.size());
        assertEquals("Value1", list.get(0).getValue());
        assertEquals("Value2", list.get(1).getValue());
    }

    @Test
    public void testToString() {
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        assertEquals("Key1=\"Value1\" Key2=\"Value2\"", attributes.toString());
    }

    @Test
    public void testIsSet() {
        attributes.put("Key1", true);
        assertTrue(attributes.get("Key1").isSet());

        attributes.put("Key1", false);
        assertFalse(attributes.get("Key1").isSet());

        attributes.remove("Key1");
        assertFalse(attributes.get("Key1") != null ? attributes.get("Key1").isSet() : false);
    }
}