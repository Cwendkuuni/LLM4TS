package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmpty() {
        assertEquals("", attributes.get("empty"));
    }

    @Test
    public void testGetCaseSensitive() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        attributes.put("key", "value");
        attributes.get(null);
    }

    @Test
    public void testPutGetReplace() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutGetAdd() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutOverride() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCaseReplace() {
        attributes.put("Key", "Value1");
        attributes.put("key", "Value2");
        assertEquals("Value2", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPutIgnoreCaseAdd() {
        attributes.put("Key", "Value1");
        attributes.put("key", "Value2");
        assertEquals("Value1", attributes.getIgnoreCase("key"));
        assertEquals("Value2", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPutIgnoreCaseCollision() {
        attributes.put("Key1", "Value1");
        attributes.put("key1", "Value2");
        assertEquals("Value1", attributes.getIgnoreCase("Key1"));
        assertEquals("Value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("booleanKey", true);
        assertTrue(attributes.hasKey("booleanKey"));
        assertFalse(attributes.get("booleanKey").isEmpty());
        assertFalse(attributes.get("booleanKey").equals(""));
        assertEquals("", attributes.get("booleanKey").toLowerCase());
    }

    @Test
    public void testPutBooleanRemove() {
        attributes.put("booleanKey", true);
        attributes.remove("booleanKey");
        assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        attributes.addAll(attributes1);
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSizeNegative() {
        attributes.size(-1);
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            entries.add(entry);
        }

        assertEquals(2, entries.size());
        assertEquals("key1", entries.get(0).getKey());
        assertEquals("value1", entries.get(0).getValue());
        assertEquals("key2", entries.get(1).getKey());
        assertEquals("value2", entries.get(1).getValue());
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("key1", list.get(0).getKey());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("key2", list.get(1).getKey());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("key1", dataset.keySet().iterator().next());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("key2", dataset.keySet().iterator().next());
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder sb = new StringBuilder();
        attributes.html(sb, null);

        String expected = " key1=\"value1\" key2=\"value2\"";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = attributes1.clone();
        assertEquals("value1", attributes2.get("key1"));
        assertFalse(attributes1.equals(attributes2));
    }
}