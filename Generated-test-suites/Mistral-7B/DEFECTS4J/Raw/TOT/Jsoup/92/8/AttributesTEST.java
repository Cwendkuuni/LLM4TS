package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    @Test
    public void testGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);

        attributes.put("Key", "VALUE");
        result = attributes.getIgnoreCase("key");
        assertEquals("value", result);
        result = attributes.getIgnoreCase("Key");
        assertEquals("VALUE", result);
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "newValue");
        String result = attributes.get("key");
        assertEquals("newValue", result);

        attributes.put("Key", "VALUE");
        attributes.putIgnoreCase("key", "newValue");
        result = attributes.get("key");
        assertEquals("newValue", result);
        result = attributes.getIgnoreCase("Key");
        assertEquals("VALUE", result);

        attributes.put("key", null);
        attributes.putIgnoreCase("key", null);
        result = attributes.get("key");
        assertNull(result);
        result = attributes.getIgnoreCase("key");
        assertNull(result);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key") != null);
        assertFalse(attributes.hasKeyIgnoreCase("Key"));

        attributes.put("Key", false);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key") != null);
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));

        attributes.putIgnoreCase("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.get("key") != null);
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "newValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.putIgnoreCase("key", "newValue");
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testRemoveAttributeWithValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));

        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
        assertNull(attributes.get("Key"));
        assertNull(attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testHasAttribute() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));

        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));

        attributes.remove("key");
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());

        attributes.remove("key1");
        assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes2.put("key3", "value3");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        assertEquals(1, attributes3.size());
        assertFalse(attributes3.hasKey("key2"));
        assertFalse(attributes3.hasKey("key3"));
        assertTrue(attributes3.hasKey("key1"));
        assertEquals("value1", attributes3.get("key1"));

        attributes3.addAll(attributes2);
        assertEquals(3, attributes3.size());
        assertTrue(attributes3.hasKey("key1"));
        assertTrue(attributes3.hasKey("key2"));
        assertTrue(attributes3.hasKey("key3"));
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
        assertEquals("value3", attributes3.get("key3"));
    }

    @Test
    public void testGetHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals("<key1>value1</key1><key2>value2</key2>", attributes.html());

        attributes.remove("key1");
        assertEquals("<key2>value2</key2>", attributes.html());

        attributes.put("key3", null);
        assertEquals("<key2>value2</key2><key3>", attributes.html());

        attributes.normalize();
        assertEquals("<key2>value2</key2><key3>", attributes.html());
    }

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
}