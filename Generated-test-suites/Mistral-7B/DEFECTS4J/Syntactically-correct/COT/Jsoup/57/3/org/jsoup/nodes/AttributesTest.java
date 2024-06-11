package org.jsoup.nodes; // Import the package of the class under test

import static org.junit.Assert.*;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class AttributesTest {
    

    // Test for the get method
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonExistentKey"));
    }

    // Test for the getIgnoreCase method
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("NonExistentKey"));
    }

    // Test for the put method
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    // Test for the put method with boolean value
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.size() > 1);
    }

    // Test for the put method with Attribute object
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    // Test for the remove method
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    // Test for the removeIgnoreCase method
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("Key1");
        assertEquals("value2", attributes.get("key2"));
    }

    // Test for the hasKey method
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    // Test for the hasKeyIgnoreCase method
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    // Test for the size method
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    // Test for the addAll method
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

    // Test for the iterator method
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute : attributesList) {
            assertEquals(attribute.getKey(), "key1" || "key2");
        }
    }

    // Test for the asList method
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        assertTrue(attributesList.get(0).equals(new Attribute("key1", "value1")));
        assertTrue(attributesList.get(1).equals(new Attribute("key2", "value2")));
    }

    // Test for the dataset method
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    // Test for the html method
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    // Test for the equals method
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

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
    }

    // Test for the hashCode method
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    // Test for the clone method
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        assertTrue(attributes.equals(clonedAttributes));
    }

    // Test for the Dataset class
    @Test
    public void testDatasetClass() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));

        Validate.notNull(dataset); // Test for null
        dataset.clear(); // Test for empty
    }

    // Test for edge cases and exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value"); // Null key is not allowed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value"); // Empty key is not allowed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null); // Null value is not allowed
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationException() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        try {
            attributes.html(null, null); // OutputSettings should not be null
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    
}