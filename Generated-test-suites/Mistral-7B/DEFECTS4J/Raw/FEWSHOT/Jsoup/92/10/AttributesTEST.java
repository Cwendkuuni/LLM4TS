package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals(3, attributes.size());
    }

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.put("Key3", "Value3");

        assertEquals("Value1", attributes.getIgnoreCase("key1"));
        assertEquals("Value2", attributes.getIgnoreCase("key2"));
        assertEquals("Value3", attributes.getIgnoreCase("key3"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3");

        assertEquals("value3", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));

        attributes.put("key2", "value4");

        assertEquals("value4", attributes.get("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.put("KEY3", "Value3");

        assertEquals("Value3", attributes.getIgnoreCase("key3"));
        assertEquals("Value2", attributes.getIgnoreCase("key2"));
        assertEquals("Value1", attributes.getIgnoreCase("key1"));

        attributes.putIgnoreCase("Key1", "Value4");

        assertEquals("Value4", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertEquals(Boolean.TRUE, attributes.get("key"));
        assertEquals("", attributes.get("key").toString());

        attributes.put("key", false);
        assertEquals(Boolean.FALSE, attributes.get("key"));
        assertEquals("", attributes.get("key").toString());

        attributes.remove("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertTrue(attributes.hasKey("key3"));
        assertFalse(attributes.hasKey("nonExistentKey"));

        attributes.putIgnoreCase("KEY1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.put("Key3", "Value3");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertTrue(attributes.hasKeyIgnoreCase("Key2"));
        assertTrue(attributes.hasKeyIgnoreCase("key3"));
        assertTrue(attributes.hasKeyIgnoreCase("Key3"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertNull(attributes.get("key1"));

        attributes.remove("nonExistentKey");
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertNull(attributes.get("nonExistentKey"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        attributes.put("Key3", "Value3");

        attributes.removeIgnoreCase("key1");
        assertEquals("Value2", attributes.get("key2"));
        assertEquals("Value3", attributes.get("key3"));
        assertNull(attributes.get("key1"));

        attributes.removeIgnoreCase("KEY1");
        assertEquals("Value2", attributes.get("key2"));
        assertEquals("Value3", attributes.get("key3"));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(4, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));
        assertTrue(attributes3.hasKey("key4"));

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
        assertEquals("value4", attributes3.get("key4"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();
        while (iterator.hasNext()) {
            attributesList.add(iterator.next());
        }

        assertEquals(3, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("key2", attributesList.get(1).getKey());
        assertEquals("value2", attributesList.get(1).getValue());
        assertEquals("key3", attributesList.get(2).getKey());
        assertEquals("value3", attributesList.get(2).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(3, attributesList.size());
        assertEquals("key1", attributesList.get(0).getKey());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("key2", attributesList.get(1).getKey());
        assertEquals("value2", attributesList.get(1).getValue());
        assertEquals("key3", attributesList.get(2).getKey());
        assertEquals("value3", attributesList.get(2).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("data-key3", "value3");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(3, dataset.size());
        assertEquals("key1", dataset.get("key1"));
        assertEquals("value1", dataset.get("value1"));
        assertEquals("key2", dataset.get("key2"));
        assertEquals("value2", dataset.get("value2"));
        assertEquals("key3", dataset.get("key3"));
        assertEquals("value3", dataset.get("value3"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\" key3=\"value3\"");

        String actualHtml = attributes.html();

        assertEquals(expectedHtml.toString(), actualHtml);
    }

    @Test
    public void testToString() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        String expectedToString = new StringBuilder()
                .append("Attributes{size=3, keys=[key1, key2, key3], vals=[value1, value2, value3]}")
                .toString();

        String actualToString = attributes.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes1.put("key3", "value3");

        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes3.put("key1", "value1");
        attributes3.put("key4", "value4");
        attributes3.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes1.put("key3", "value3");

        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes3.put("key1", "value1");
        attributes3.put("key4", "value4");
        attributes3.put("key3", "value3");

        int attributes1HashCode = attributes1.hashCode();
        int attributes2HashCode = attributes2.hashCode();
        int attributes3HashCode = attributes3.hashCode();

        assertEquals(attributes1HashCode, attributes1HashCode);
        assertEquals(attributes1HashCode, attributes2HashCode);
        assertNotEquals(attributes1HashCode, attributes3HashCode);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key3", "value3");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(3, clonedAttributes.size());
        assertEquals("key1", clonedAttributes.get("key1"));
        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("key2", clonedAttributes.get("key2"));
        assertEquals("value2", clonedAttributes.get("key2"));
        assertEquals("key3", clonedAttributes.get("key3"));
        assertEquals("value3", clonedAttributes.get("key3"));
    }
}