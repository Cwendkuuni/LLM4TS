package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;
    private static final String key1 = "key1";
    private static final String key2 = "key2";
    private static final String key3 = "key3";
    private static final String key4 = "key4";
    private static final String value1 = "value1";
    private static final String value2 = "value2";
    private static final String value3 = "value3";
    private static final String value4 = "value4";

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testNewInstance() {
        assertEquals(0, attributes.size());
    }

    @Test
    public void testGet() {
        attributes.put(key1, value1);
        assertEquals(value1, attributes.get(key1));
        assertEquals("", attributes.get("nonexistentkey"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put(key1, value1);
        assertEquals(value1, attributes.getIgnoreCase(key1));
        assertEquals("", attributes.getIgnoreCase("nonexistentkey"));

        attributes.put(key2, value2);
        assertEquals(value1, attributes.getIgnoreCase(key1));
        assertEquals(value2, attributes.getIgnoreCase(key2));
    }

    @Test
    public void testPut() {
        attributes.put(key1, value1);
        assertEquals(value1, attributes.get(key1));

        attributes.put(key1, value2);
        assertEquals(value2, attributes.get(key1));

        attributes.put(key2, value3);
        assertEquals(value2, attributes.get(key1));
        assertEquals(value3, attributes.get(key2));
    }

    @Test
    public void testPutFalseValue() {
        attributes.put(key1, value1);
        attributes.put(key1, false);
        assertEquals("", attributes.get(key1));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put(key1, value1);
        attributes.put(key1, true);
        assertEquals(" true", attributes.get(key1));

        attributes.put(key1, false);
        assertEquals("", attributes.get(key1));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute(key1, value1);
        attributes.put(attribute);
        assertEquals(value1, attributes.get(key1));

        Attribute attribute2 = new Attribute(key2, value2);
        attributes.put(attribute2);
        assertEquals(value2, attributes.get(key2));
    }

    @Test
    public void testRemove() {
        attributes.put(key1, value1);
        assertEquals(value1, attributes.get(key1));
        attributes.remove(key1);
        assertEquals("", attributes.get(key1));

        attributes.put(key1, value1);
        attributes.put(key2, value2);
        attributes.remove(key1);
        assertEquals(value2, attributes.get(key2));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put(key1, value1);
        assertEquals(value1, attributes.get(key1));
        attributes.removeIgnoreCase(key1);
        assertEquals("", attributes.get(key1));

        attributes.put(key1, value1);
        attributes.put(key2, value2);
        attributes.removeIgnoreCase(key1);
        assertEquals(value2, attributes.get(key2));
    }

    @Test
    public void testHasKey() {
        attributes.put(key1, value1);
        assertTrue(attributes.hasKey(key1));
        assertFalse(attributes.hasKey("nonexistentkey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put(key1, value1);
        assertTrue(attributes.hasKeyIgnoreCase(key1));
        assertTrue(attributes.hasKeyIgnoreCase(key1.toUpperCase()));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put(key1, value1);
        assertEquals(1, attributes.size());
        attributes.put(key2, value2);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put(key3, value3);
        attributes2.put(key4, value4);

        attributes.addAll(attributes2);
        assertEquals(key3, attributes.getIgnoreCase(key3));
        assertEquals(value3, attributes.getIgnoreCase(key3));
        assertEquals(key4, attributes.getIgnoreCase(key4));
        assertEquals(value4, attributes.getIgnoreCase(key4));
    }

    @Test
    public void testIterator() {
        attributes.put(key1, value1);
        attributes.put(key2, value2);

        List<Attribute> list = new ArrayList<>();
        for (Attribute attribute : attributes) {
            list.add(attribute);
        }

        assertEquals(2, list.size());
        assertSame(attributes.get(key1), list.get(0));
        assertSame(attributes.get(key2), list.get(1));
    }

    @Test
    public void testAsList() {
        attributes.put(key1, value1);
        attributes.put(key2, value2);

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertSame(attributes.get(key1), list.get(0));
        assertSame(attributes.get(key2), list.get(1));
    }

    @Test
    public void testDataset() {
        attributes.put(dataPrefix + key1, value1);
        attributes.put(dataPrefix + key2, value2);

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals(value1, dataset.get(key1));
        assertEquals(value2, dataset.get(key2));
    }

    @Test
    public void testHtml() {
        attributes.put(key1, value1);
        StringBuilder accum = new StringBuilder();
        try {
            attributes.html(accum, null);
        } catch (Exception e) {
            fail("should not throw an exception when generating HTML with no OutputSettings");
        }

        assertEquals(" ", accum.toString());

        StringBuilder accum2 = new StringBuilder();
        try {
            attributes.html(accum2, null);
            attributes.html(accum2, new Document.OutputSettings().syntax(Document.OutputSettings.SyntacticMode.XML));
        } catch (IOException e) {
            fail("should not throw an exception when generating HTML in XML mode");
        }

        assertEquals("<map/>", accum2.toString());

        StringBuilder accum3 = new StringBuilder();
        try {
            attributes.html(accum3, new Document.OutputSettings().syntax(Document.OutputSettings.SyntacticMode.HTML));
        } catch (IOException e) {
            fail("should not throw an exception when generating HTML in HTML mode");
        }

        assertEquals("<map " + key1 + "=\"" + value1 + "\"></map>", accum3.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put(key1, value1);
        attributes2.put(key1, value1);
        attributes3.put(key2, value1);

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put(key1, value1);
        attributes2.put(key1, value1);
        attributes3.put(key2, value1);

        int hashcode1 = attributes1.hashCode();
        int hashcode2 = attributes2.hashCode();
        int hashcode3 = attributes3.hashCode();

        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put(key1, value1);
        attributes1.put(key2, value2);

        Attributes attributes2 = attributes1.clone();

        assertEquals(attributes1.size(), attributes2.size());
        assertEquals(attributes1.get(key1), attributes2.get(key1));
        assertEquals(attributes1.get(key2), attributes2.get(key2));

        attributes1.remove(key1);
        assertEquals(attributes1.get(key2), attributes2.get(key2));
    }
}