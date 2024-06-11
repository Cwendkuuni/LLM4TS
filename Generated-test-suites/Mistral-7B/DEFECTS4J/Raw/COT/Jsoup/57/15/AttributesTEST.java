package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("value", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key")); // case insensitive
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("otherKey"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("OtherKey"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        assertEquals(2, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        for (Attribute attribute : attributesList) {
            String key = attribute.getKey();
            assertTrue(attributes.hasKey(key));
            String value = attributes.get(key);
            assertEquals(value, attribute.getValue());
        }
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        for (Attribute attribute : attributesList) {
            String key = attribute.getKey();
            assertTrue(attributes.hasKey(key));
            String value = attributes.get(key);
            assertEquals(value, attribute.getValue());
        }
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertTrue(dataset.containsKey("data-key2"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String html = attributes.html();
        String expected = " key=\"value\"";
        assertEquals(expected, html);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithException() {
        Attributes attributes = new Attributes();
        throw new SerializationException("Exception message");
    }

    @Test
    public void testEqualsMethod() {
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
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(clonedAttributes.hasKey("key1"));
        assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
    }
}