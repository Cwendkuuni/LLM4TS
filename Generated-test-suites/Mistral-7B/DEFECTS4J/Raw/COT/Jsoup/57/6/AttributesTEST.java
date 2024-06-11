package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testGetNull() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("notExist"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.hasKey("booleanKey") && attributes.size() > 1);
        assertNull(attributes.get("booleanKey")); // get should return null since the values are not stored
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasNoKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("notExist"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testEmptyIterator() {
        Attributes attributes = new Attributes();
        List<Attribute> list = attributes.asList();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("dataValue1", dataset.get("dataKey1"));
    }

    @Test(expected = SerializationException.class)
    public void testHtmlNull() {
        Attributes attributes = new Attributes();
        attributes.html(null, null);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlIOException() throws IOException {
        Attributes attributes = new Attributes();
        StringBuilder accum = new StringBuilder();
        Document document = new Document("", null);
        document.outputSettings().syntax("xml"); // for testing purpose
        attributes.html(accum, document.outputSettings());
        throw new IOException();
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder accum = new StringBuilder();
        Document document = new Document("", null);
        document.outputSettings().syntax("xml"); // for testing purpose
        attributes.html(accum, document.outputSettings());
        assertEquals("<key1>value1</key1>", accum.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Attributes clone = attributes.clone();
        assertEquals(attributes, clone);
    }

    @Test
    public void testValidateArguments() {
        Attributes attributes = new Attributes();
        String emptyKey = "";
        String nullKey = null;
        String nullValue = null;

        // check get
        attributes.get(emptyKey);
        attributes.get(nullKey);

        // check put
        attributes.put(emptyKey, nullValue);
        attributes.put(nullKey, nullValue);

        // check remove
        attributes.remove(emptyKey);
        attributes.remove(nullKey);

        // check hasKey
        attributes.hasKey(emptyKey);
        attributes.hasKey(nullKey);

        // check hasKeyIgnoreCase
        attributes.hasKeyIgnoreCase(emptyKey);
        attributes.hasKeyIgnoreCase(nullKey);

        // check size
        attributes.size();

        // check addAll
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.addAll(attributes2);
        attributes1.addAll(attributes3);

        // check iterator
        Iterator<Attribute> iterator = attributes.iterator();
        assertNull(iterator.next());

        // check asList
        List<Attribute> asList = attributes.asList();
        assertNull(asList.get(0));

        // check dataset
        Map<String, String> dataset = attributes.dataset();
        assertNull(dataset.get("notExist"));

        // check html
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
    }

    @Test
    public void testDataPrefix() {
        Attributes attributes = new Attributes();
        String dataKey = "dataKey";
        attributes.put(dataKey, "value");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("data:" + dataKey, dataset.get(dataKey));
    }
}