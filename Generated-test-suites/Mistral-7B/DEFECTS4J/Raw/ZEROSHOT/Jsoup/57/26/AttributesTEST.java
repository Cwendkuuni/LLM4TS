package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "testValue");
        assertEquals("testValue", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.get("test").getValue().equals("true"));
        assertFalse(attributes.hasKey("TEST"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("test", "testValue");
        attributes.put(attribute);
        assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        attributes.remove("test");
        assertNull(attributes.get("test"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "testValue");
        attributes.removeIgnoreCase("test");
        assertNull(attributes.getIgnoreCase("test"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKey("TEST"));
        assertFalse(attributes.hasKeyIgnoreCase("TEST"));
        assertFalse(attributes.hasKeyIgnoreCase("test2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("test", "testValue");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(2, attributes3.size());
        assertEquals("testValue1", attributes3.get("test1"));
        assertEquals("testValue2", attributes3.get("test2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        List<Map.Entry<String, String>> list = Arrays.asList(
                new AbstractMap.SimpleEntry<>("test1", "testValue1"),
                new AbstractMap.SimpleEntry<>("test2", "testValue2")
        );

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();

        assertEquals(list.size(), iterator.size());

        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, String> entry = iterator.next();
            Map.Entry<String, String> expectedEntry = list.get(i);
            assertEquals(expectedEntry.getKey(), entry.getKey());
            assertEquals(expectedEntry.getValue(), entry.getValue());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        List<Attribute> list = attributes.asList();

        for (Attribute attribute : list) {
            System.out.println(attribute.getKey() + ": " + attribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "testValue1");
        attributes.put("data-test2", "testValue2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("testValue1", dataset.get("data-test1"));
        assertEquals("testValue2", dataset.get("data-test2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String html = attributes.html();

        assertEquals("<test>testValue</test>", html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "testValue1");

        Attributes attributes3 = new Attributes();
        attributes3.put("test2", "testValue2");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes, clonedAttributes);
    }
}