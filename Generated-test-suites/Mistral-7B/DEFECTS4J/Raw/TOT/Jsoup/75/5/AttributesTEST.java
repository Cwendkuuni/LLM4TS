package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("id", "testId");
        attributes.put("title", "Test Title");
        attributes.put("data-custom", "customValue");
    }

    @Test
    public void testGetAttribute() {
        assertEquals("test", attributes.get("class"));
        assertEquals("testId", attributes.get("id"));
        assertEquals("Test Title", attributes.get("title"));
        assertEquals("customValue", attributes.get("data-custom"));
        assertEquals(EmptyString, attributes.get("nonexistent"));
    }

    @Test
    public void testGetAttributeIgnoreCase() {
        assertEquals("test", attributes.getIgnoreCase("Class"));
        assertEquals("testId", attributes.getIgnoreCase("id"));
        assertEquals("Test Title", attributes.getIgnoreCase("title"));
        assertEquals("customValue", attributes.getIgnoreCase("Data-custom"));
        assertEquals(EmptyString, attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPutAttribute() {
        attributes.put("newKey", "newValue");
        assertEquals("newValue", attributes.get("newKey"));
        assertEquals("newValue", attributes.getIgnoreCase("NewKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test
    public void testPutAttributeWithExistingKey() {
        attributes.put("class", "existingValue");
        attributes.put("class", "newValue");
        assertEquals("newValue", attributes.get("class"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanAttribute", true);
        assertTrue(attributes.hasKey("booleanAttribute"));
        assertFalse(attributes.get("booleanAttribute").isEmpty());
        assertFalse(attributes.get("booleanAttribute").equals(EmptyString));
        assertEquals(EmptyString, attributes.getIgnoreCase("BooleanAttribute"));
    }

    @Test
    public void testPutBooleanAttributeRemoveExisting() {
        attributes.put("booleanAttribute", true);
        attributes.put("booleanAttribute", false);
        assertFalse(attributes.hasKey("booleanAttribute"));
    }

    @Test
    public void testPutAttributeWithDataPrefix() {
        attributes.put("data-custom", "value");
        assertEquals("value", attributes.get("data-custom"));
        assertEquals("value", attributes.getIgnoreCase("Data-custom"));
    }

    @Test
    public void testRemoveAttribute() {
        attributes.remove("class");
        assertFalse(attributes.hasKey("class"));
    }

    @Test
    public void testRemoveAttributeIgnoreCase() {
        attributes.put("Class", "value");
        attributes.remove("Class");
        assertFalse(attributes.hasKey("Class"));
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        attributes.remove("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKeyIgnoreCase("class"));
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKeyIgnoreCase("id"));
        assertTrue(attributes.hasKey("title"));
        assertTrue(attributes.hasKeyIgnoreCase("title"));
        assertTrue(attributes.hasKey("data-custom"));
        assertTrue(attributes.hasKeyIgnoreCase("Data-custom"));
        assertFalse(attributes.hasKey("nonexistent"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testHasKeyWithDataPrefix() {
        attributes.put("data-custom", "value");
        assertTrue(attributes.hasKey("data-custom"));
        assertTrue(attributes.hasKeyIgnoreCase("Data-custom"));
        assertFalse(attributes.hasKey("custom"));
        assertFalse(attributes.hasKeyIgnoreCase("Custom"));
    }

    @Test
    public void testSize() {
        assertEquals(4, attributes.size());
    }

    @Test
    public void testPutAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey1", "newValue1");
        newAttributes.put("newKey2", "newValue2");
        attributes.putAll(newAttributes);

        assertEquals("newValue1", attributes.get("newKey1"));
        assertEquals("newValue1", attributes.getIgnoreCase("NewKey1"));
        assertEquals("newValue2", attributes.get("newKey2"));
        assertEquals("newValue2", attributes.getIgnoreCase("NewKey2"));
    }

    @Test
    public void testIterator() {
        List<Attribute> attributesList = attributes.asList();
        Iterator<Attribute> iterator = attributesList.iterator();
        assertEquals("class", iterator.next().getKey());
        assertEquals("test", iterator.next().getValue());
        assertEquals("id", iterator.next().getKey());
        assertEquals("testId", iterator.next().getValue());
        assertEquals("title", iterator.next().getKey());
        assertEquals("Test Title", iterator.next().getValue());
        assertEquals("data-custom", iterator.next().getKey());
        assertEquals("customValue", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals("test", dataset.get("class"));
        assertEquals("testId", dataset.get("id"));
        assertEquals("Test Title", dataset.get("title"));
        assertEquals("customValue", dataset.get("data-custom"));
        assertFalse(dataset.containsKey("nonexistent"));
    }

    @Test
    public void testHtml() {
        StringBuilder expectedHtml = new StringBuilder();
        attributes.html(expectedHtml, new Document.OutputSettings());
        String actualHtml = attributes.html();
        assertEquals(expectedHtml.toString(), actualHtml);
    }
}