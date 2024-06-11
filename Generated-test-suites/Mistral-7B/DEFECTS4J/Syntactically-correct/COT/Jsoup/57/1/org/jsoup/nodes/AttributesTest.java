package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key").isDataAttribute());
        assertNull(attributes.get("key"));
        attributes.remove("key");
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
        assertEquals(attribute, attributes.asList().get(0));
        attributes.remove("key");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        attributes.put("key", "value");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(attributes1);
        mergedAttributes.addAll(attributes2);

        List<Attribute> mergedList = mergedAttributes.asList();
        List<Attribute> attributes1List = attributes1.asList();
        List<Attribute> attributes2List = attributes2.asList();

        assertEquals(4, mergedAttributes.size());
        assertEquals(attributes1List, mergedList.subList(0, 2));
        assertEquals(attributes2List, mergedList.subList(2, 4));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expected = new ArrayList<>();
        expected.add(new Attribute("key1", "value1"));
        expected.add(new Attribute("key2", "value2"));

        List<Attribute> actual = new ArrayList<>();
        for (Attribute attribute : attributes) {
            actual.add(attribute);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expected = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actual = attributes.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("key2", "value2");

        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("data-key1", "data-value1");

        Map<String, String> actual = attributes.dataset();

        assertEquals(expected, actual);
    }

    @Test(expected = SerializationException.class)
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.html(null, null);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        Attributes attributes3 = new Attributes();
        attributes3.put("key", "value");

        Attributes attributes4 = new Attributes();
        attributes4.put("key1", "value1");
        attributes4.put("key2", "value2");

        Attributes attributes5 = new Attributes();
        attributes5.put("key1", "value1");
        attributes5.put("key2", "value2");
        attributes5.putAll(attributes3);

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes1));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
        assertFalse(attributes1.equals(new Attributes()));

        assertFalse(attributes1.equals(attributes2));
        assertFalse(attributes2.equals(attributes1));

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes1));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
        assertFalse(attributes1.equals(new Attributes()));

        assertTrue(attributes3.equals(attributes5));
        assertTrue(attributes5.equals(attributes3));

        assertFalse(attributes1.equals(attributes4));
        assertFalse(attributes4.equals(attributes1));

        Attributes attributes6 = new Attributes(attributes4);
        assertTrue(attributes4.equals(attributes6));
        assertTrue(attributes6.equals(attributes4));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        Attributes attributes3 = new Attributes();
        attributes3.put("key", "value");

        Attributes attributes4 = new Attributes();
        attributes4.put("key1", "value1");
        attributes4.put("key2", "value2");

        Attributes attributes5 = new Attributes();
        attributes5.put("key1", "value1");
        attributes5.put("key2", "value2");
        attributes5.putAll(attributes3);

        int hashcode1 = attributes1.hashCode();
        int hashcode2 = attributes1.hashCode();
        assertNotSame(hashcode1, hashcode2);
        assertEquals(hashcode1, hashcode2);

        int hashcode3 = attributes3.hashCode();
        int hashcode5 = attributes5.hashCode();
        assertEquals(hashcode3, hashcode5);

        assertNotSame(attributes1.hashCode(), attributes2.hashCode());
        assertNotSame(attributes1.hashCode(), attributes3.hashCode());
        assertNotSame(attributes1.hashCode(), attributes4.hashCode());
        assertNotSame(attributes1.hashCode(), attributes5.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = attributes1.clone();

        List<Attribute> attributes1List = attributes1.asList();
        List<Attribute> attributes2List = attributes2.asList();

        assertEquals(2, attributes1.size());
        assertEquals(2, attributes2.size());
        assertEquals(attributes1List, attributes2List);
    }
}