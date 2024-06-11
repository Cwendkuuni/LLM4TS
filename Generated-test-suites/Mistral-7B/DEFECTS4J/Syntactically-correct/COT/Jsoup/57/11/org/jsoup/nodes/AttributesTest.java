package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Dataset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for Attributes class.
 */
public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        attributes.put("key2", "value2");
        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        String emptyString = attributes.get("nonExistentKey");
        assertEquals("", emptyString);

        attributes = new Attributes();
        String emptyAttributes = attributes.get("nonExistentKey");
        assertEquals("", emptyAttributes);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String value1 = attributes.getIgnoreCase("key1");
        assertEquals("value1", value1);

        attributes.put("key2", "value2");
        String value2 = attributes.getIgnoreCase("key2");
        assertEquals("value2", value2);

        String emptyString = attributes.getIgnoreCase("nonExistentKey");
        assertEquals("", emptyString);

        attributes = new Attributes();
        String emptyAttributes = attributes.getIgnoreCase("nonExistentKey");
        assertEquals("", emptyAttributes);

        attributes = new Attributes();
        attributes.put("KEY1", "value1");
        String value1IgnoreCase = attributes.getIgnoreCase("key1");
        assertEquals("value1", value1IgnoreCase);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String value1 = attributes.get("key1");
        assertEquals("value1", value1);

        attributes.put("key1", "value2");
        String value2 = attributes.get("key1");
        assertEquals("value2", value2);

        attributes.put("key2", "value2");
        String value2 = attributes.get("key2");
        assertEquals("value2", value2);

        attributes.put("key1", "value1");
        String value1AfterPut = attributes.get("key1");
        assertEquals("value1", value1AfterPut);

        attributes.put("key3", "value3");
        int size = attributes.size();
        assertEquals(3, size);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));

        attributes = new Attributes();
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key1", "value1"));
        Attribute value1 = attributes.get("key1");
        assertEquals("value1", value1.getValue());

        attributes.put(new Attribute("key1", "value2"));
        Attribute value2 = attributes.get("key1");
        assertEquals("value2", value2.getValue());

        attributes.put(new Attribute("key2", "value2"));
        Attribute value2 = attributes.get("key2");
        assertEquals("value2", value2.getValue());

        attributes.put(new Attribute("key1", "value1"));
        Attribute value1AfterPut = attributes.get("key1");
        assertEquals("value1", value1AfterPut.getValue());

        attributes.put(new Attribute("key3", "value3"));
        int size = attributes.size();
        assertEquals(3, size);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));

        attributes = new Attributes();
        attributes.put("KEY1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));

        attributes.put("key2", "value2");
        assertTrue(attributes.hasKey("key2"));

        attributes.put("key1", "value2");
        assertTrue(attributes.hasKey("key1"));

        attributes = new Attributes();
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("KEY1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));

        attributes.put("key2", "value2");
        assertFalse(attributes.hasKeyIgnoreCase("key1"));

        attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        int size = attributes.size();
        assertEquals(0, size);

        attributes.put("key1", "value1");
        int sizeAfterPut = attributes.size();
        assertEquals(1, sizeAfterPut);

        attributes.put("key2", "value2");
        int sizeAfterPut2 = attributes.size();
        assertEquals(2, sizeAfterPut2);

        attributes = new Attributes();
        int sizeEmpty = attributes.size();
        assertEquals(0, sizeEmpty);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        int size1 = attributes1.size();

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        int size2 = attributes2.size();

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        int size3 = attributes3.size();
        assertEquals(size1 + size2, size3);

        List<Attribute> list = attributes3.asList();
        assertEquals(size1 + size2, list.size());

        Attributes attributes4 = new Attributes();
        attributes4.addAll(attributes3);
        int size4 = attributes4.size();
        assertEquals(size1 + size2, size4);

        List<Attribute> list2 = attributes4.asList();
        assertEquals(size1 + size2, list2.size());

        attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes3 = new Attributes();
        attributes3.addAll(Collections.emptyList());
        attributes4 = new Attributes();
        attributes4.addAll(attributes1);
        attributes4.addAll(attributes2);
        attributes4.addAll(attributes3);
        int size5 = attributes4.size();
        assertEquals(size1 + size2 + size3, size5);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Dataset dataset = attributes.dataset();
        Map<String, String> map = (Map<String, String>) dataset;
        assertEquals(2, map.size());

        String value1 = map.get("key1");
        assertEquals("value1", value1);

        String value2 = map.get("key2");
        assertEquals("value2", value2);

        Map<String, String> emptyMap = new HashMap<>();
        Dataset emptyDataset = new Attributes().dataset();
        assertEquals(emptyMap, emptyDataset);

        Attributes attributes2 = new Attributes();
        Dataset dataset2 = attributes2.dataset();
        assertTrue(dataset2.isEmpty());

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        Dataset dataset3 = attributes3.dataset();
        Map<String, String> map3 = (Map<String, String>) dataset3;
        assertEquals(1, map3.size());
        String value3 = map3.get("key1");
        assertEquals("value1", value3);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder accum = new StringBuilder();
        attributes.html(accum, new Document("").outputSettings());

        String expectedHtml = " key1=\"value1\" key2=\"value2\"";
        String actualHtml = accum.toString();
        assertEquals(expectedHtml, actualHtml);

        Attributes emptyAttributes = new Attributes();
        StringBuilder emptyAccum = new StringBuilder();
        emptyAttributes.html(emptyAccum, new Document("").outputSettings());
        String emptyExpectedHtml = "";
        String emptyActualHtml = emptyAccum.toString();
        assertEquals(emptyExpectedHtml, emptyActualHtml);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        assertFalse(attributes1.equals(attributes2));

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));

        attributes1.put("key2", "value2");
        assertFalse(attributes1.equals(attributes2));

        attributes2.put("key2", "value2");
        assertTrue(attributes1.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes3));

        Attributes attributes4 = new Attributes();
        assertFalse(attributes1.equals(attributes4));

        Attributes attributes5 = new Attributes();
        Attributes attributes6 = new Attributes();
        attributes5.putAll(attributes6);
        assertTrue(attributes5.equals(attributes6));
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertTrue(attributes.equals(clonedAttributes));

        attributes.put("key3", "value3");
        attributes.hashCode();
        clonedAttributes.put("key3", "value3");
        assertTrue(attributes.equals(clonedAttributes));
        assertNotSame(attributes, clonedAttributes);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneMethodException() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.clone();
    }
}