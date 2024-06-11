package org.jsoup.nodes; // make sure the test package matches the class package

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
    // basic test cases for each method

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        String actual = attributes.get("color");
        assertEquals("red", actual);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        String actual = attributes.getIgnoreCase("Color");
        assertEquals("red", actual);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        String actual = attributes.get("color");
        assertEquals("red", actual);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Color", "red");
        String actual = attributes.get("color");
        assertEquals("red", actual);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        assertTrue(attributes.hasKey("checked"));
        assertFalse(attributes.get("checked").isEmpty());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.remove("color");
        assertFalse(attributes.hasKey("color"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        attributes.removeIgnoreCase("Color");
        assertFalse(attributes.hasKey("Color"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        assertTrue(attributes.hasKey("color"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Color", "red");
        assertTrue(attributes.hasKeyIgnoreCase("Color"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("color1", "red1");
        attributes1.put("size1", "medium1");

        Attributes attributes2 = new Attributes();
        attributes2.put("color2", "red2");
        attributes2.put("size2", "medium2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(4, result.size());
        assertTrue(result.hasKey("color1"));
        assertTrue(result.hasKey("size1"));
        assertTrue(result.hasKey("color2"));
        assertTrue(result.hasKey("size2"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        Attribute attribute1 = list.get(0);
        assertEquals("color", attribute1.getKey());
        assertEquals("red", attribute1.getValue());

        Attribute attribute2 = list.get(1);
        assertEquals("size", attribute2.getKey());
        assertEquals("medium", attribute2.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-color", "red");
        attributes.put("data-size", "medium");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("data-color"));
        assertTrue(dataset.containsKey("data-size"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("size", "medium");

        String html = attributes.html();
        String expected = " color=\"red\" size=\"medium\"";
        assertEquals(expected, html);
    }

    // edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testNullKeyPut() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValuePut() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringKeyPut() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringValuePut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("color", "blue"); // should override the previous value
        assertEquals("blue", attributes.get("color"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveInvalidIndex() {
        Attributes attributes = new Attributes();
        attributes.remove(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalKeyCharacter() {
        Attributes attributes = new Attributes();
        attributes.put("key#", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeyTooLong() {
        String longKey = new String(new char[1025])[0];
        Attributes attributes = new Attributes();
        attributes.put(longKey, "value");
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("color", "red");
        attributes.put("COLOR", "blue");
        attributes.normalize();
        assertEquals("red", attributes.get("color"));
        assertEquals("red", attributes.get("COLOR"));
    }
}