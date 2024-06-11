package org.jsoup.nodes; // make sure to import the Attributes class here

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

    

    // Basic test cases for each method

    @Test
    public void testAttributesInstance() {
        Attributes attributes = new Attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(EMPTY_STRING, attributes.get("nonexistentkey"));
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals(EMPTY_STRING, attributes.getIgnoreCase("NONEEXISTENTKEY"));
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(EMPTY_STRING, attributes.get("nonexistentkey"));

        attributes.put("key", "newvalue");
        assertEquals("newvalue", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals("Value", attributes.getIgnoreCase("KEY"));

        attributes.putIgnoreCase("key", "newvalue");
        assertEquals("newvalue", attributes.getIgnoreCase("key"));
        assertEquals("newvalue", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPutMethodWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertFalse(attributes.get("key").equals("key"));
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPutMethodWithBooleanValueAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        attributes.remove("key1");
        assertEquals("value2", attributes.get("key2"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        attributes.removeIgnoreCase("Key1");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertFalse(attributes.hasKey("Key1"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistentkey"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("NONEEXISTENTKEY"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
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

        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals(2, attributes3.size());
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();

        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            attributesList.add(attribute);
        }

        assertEquals(2, attributesList.size());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(2, attributesList.size());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String html = attributes.html();

        String expectedHtml = " key=\"value\"";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testEqualsAndHashcodeMethods() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value3");
        attributes3.put("key2", "value4");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertNotSame(attributes1.hashCode(), attributes2.hashCode());
        assertNotSame(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals("value1", clonedAttributes.get("key1"));
        assertEquals("value2", clonedAttributes.get("key2"));
        assertEquals(2, clonedAttributes.size());
    }

    @Test
    public void testNormalizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "VALUE");

        attributes.normalize();

        assertEquals("value", attributes.get("key"));
    }

    
}