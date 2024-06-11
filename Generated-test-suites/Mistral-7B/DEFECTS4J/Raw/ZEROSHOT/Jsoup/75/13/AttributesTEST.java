package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @org.junit.Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        String actual = attributes.getIgnoreCase("key");
        String expected = "Value";
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes actualAttributes = attributes.put("key", "newValue");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key", "newValue");
        assertEquals(expectedAttributes, actualAttributes);
    }

    @org.junit.Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Attributes actualAttributes = attributes.put("key", "newValue");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key", "newValue");
        assertEquals(expectedAttributes, actualAttributes);
    }

    @org.junit.Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean actual = attributes.get("key").equals("");
        assertTrue(actual);
    }

    @org.junit.Test
    public void testPutBooleanIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", true);
        boolean actual = attributes.getIgnoreCase("key").equals("");
        assertTrue(actual);
    }

    @org.junit.Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        assertTrue(actual);
    }

    @org.junit.Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        assertTrue(actual);
    }

    @org.junit.Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        int actualSize = attributes3.size();
        int expectedSize = 4;
        assertEquals(expectedSize, actualSize);
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value2");
        Attributes actualAttributes = attributes3.asList();
        List<Attribute> expectedList = expectedAttributes.asList();
        List<Attribute> actualList = attributes3.asList();
        for (int i = 0; i < expectedList.size(); i++) {
            Attribute expectedAttribute = expectedList.get(i);
            Attribute actualAttribute = actualList.get(i);
            assertEquals(expectedAttribute.getKey(), actualAttribute.getKey());
            assertEquals(expectedAttribute.getValue(), actualAttribute.getValue());
        }
    }

    @org.junit.Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        Attribute attribute = iterator.next();
        String key = attribute.getKey();
        String value = attribute.getValue();
        assertEquals("key1", key);
        assertEquals("value1", value);
        attribute = iterator.next();
        key = attribute.getKey();
        value = attribute.getValue();
        assertEquals("key2", key);
        assertEquals("value2", value);
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> actualList = attributes.asList();
        List<Attribute> expectedList = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
        assertEquals(expectedList, actualList);
    }

    @org.junit.Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Map<String, String> actualMap = attributes.dataset();
        Map<String, String> expectedMap = new AttributeMap(Arrays.asList(new Attribute("data-key1", "data-value1"), new Attribute("data-key2", "data-value2")));
        assertEquals(expectedMap, actualMap);
    }

    @org.junit.Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\"");
        String actualHtml = attributes.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }

    @org.junit.Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String expectedString = " key1=\"value1\"";
        String actualString = attributes.toString();
        assertEquals(expectedString, actualString);
    }

    @org.junit.Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        Attributes attributes4 = new Attributes();
        attributes4.put("key1", "value1");
        attributes4.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertTrue(attributes1.equals(attributes4));
        assertFalse(attributes1.equals(attributes3));
    }

    @org.junit.Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        assertEquals(expectedAttributes, clonedAttributes);
    }
}

class AttributeMap extends AbstractMap<String, String> {
    private final List<Attribute> attributes;

    public AttributeMap(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return new EntrySet();
    }

    @Override
    public String put(String key, String value) {
        Attribute attribute = new Attribute(key, value);
        attributes.add(attribute);
        return value;
    }

    private class EntrySet extends AbstractSet<Map.Entry<String, String>> {

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return attributes.size();
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<String, String>> {
        private Iterator<Attribute> attributeIterator = attributes.iterator();

        public boolean hasNext() {
            return attributeIterator.hasNext();
        }

        public Map.Entry<String, String> next() {
            Attribute attribute = attributeIterator.next();
            return new SimpleEntry<>(attribute.getKey(), attribute.getValue());
        }

        public void remove() {
            attributeIterator.remove();
        }
    }

    private static class SimpleEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}