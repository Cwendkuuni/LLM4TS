package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        assertEquals("value2", attributes.getIgnoreCase("KeY2"));
        assertEquals("value3", attributes.getIgnoreCase("kEy3"));
    }

    @Test
    public void testPut() {
        attributes.put("key4", "value4");
        assertEquals("value4", attributes.get("key4"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey1", true);
        assertTrue(attributes.hasKeyIgnoreCase("booleanKey1"));
        attributes.put("booleanKey2", false);
        assertFalse(attributes.hasKeyIgnoreCase("booleanKey2"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("nonexistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentKey"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
        attributes.put("key4", "value4");
        assertEquals(4, attributes.size());
        attributes.remove("key2");
        assertEquals(3, attributes.size());
    }

    @Test
    public void testIterator() {
        Iterator<Attribute> iterator = attributes.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("key1", iterator.next().getKey());
        assertTrue(iterator.hasNext());
        assertEquals("key2", iterator.next().getKey());
        assertTrue(iterator.hasNext());
        assertEquals("key3", iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsList() {
        List<Attribute> attributeList = attributes.asList();
        assertEquals(3, attributeList.size());
        assertEquals("key1", attributeList.get(0).getKey());
        assertEquals("value1", attributeList.get(0).getValue());
        assertEquals("key2", attributeList.get(1).getKey());
        assertEquals("value2", attributeList.get(1).getValue());
        assertEquals("key3", attributeList.get(2).getKey());
        assertEquals("value3", attributeList.get(2).getValue());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertTrue(dataset.isEmpty());
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("data-value1", dataset.get("key1"));
        assertEquals("data-value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        assertEquals(" key1=\"value1\" key2=\"value2\" key3=\"value3\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");
        assertEquals(attributes, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");
        assertEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        assertNotSame(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        attributes.normalize();
        assertEquals("key1", attributes.iterator().next().getKey());
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key4", "value4", attributes);
        attributes.put(attribute);
        assertEquals("value4", attributes.get("key4"));
        assertSame(attributes, attribute.parent);
    }
}