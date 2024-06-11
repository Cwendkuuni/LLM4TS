package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
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
        assertEquals("", attributes.getIgnoreCase("NonExistentKey"));
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
        assertFalse(attributes.hasKey("key")).after(attributes.remove("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attributes.Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Attributes dataset = attributes.dataset();
        assertEquals("value", dataset.get("key"));
    }

    @Test(expected = SerializationException.class)
    public void testHtml() throws SerializationException {
        Attributes attributes = new Attributes();
        attributes.html();
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes cloned = attributes.clone();
        assertEquals("value", cloned.get("key"));
    }

    @Test
    public void testValidateNotNull() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testValidateNotEmpty() {
        Attributes attributes = new Attributes();
        try {
            attributes.get("").validateNotEmpty();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetNullAttributes() {
        Attributes attributes = new Attributes(null);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCaseNullAttributes() {
        Attributes attributes = new Attributes(null);
        assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, "value");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutNullAttribute() {
        Attributes attributes = new Attributes();
        try {
            attributes.put(null, new Attributes.Attribute(null, "value"));
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.remove(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testRemoveIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        try {
            attributes.removeIgnoreCase(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHasKeyNullKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(null));
    }

    @Test
    public void testHasKeyIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase(null));
    }

    @Test
    public void testDatasetNullAttributes() {
        Attributes attributes = new Attributes(null);
        assertEquals(Collections.emptyMap(), attributes.dataset());
    }

    @Test
    public void testHtmlNullAccumulator() throws IOException {
        Attributes attributes = new Attributes();
        try {
            attributes.html(null, new Document.OutputSettings());
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHtmlNullOutputSettings() throws IOException, SerializationException {
        Attributes attributes = new Attributes();
        attributes.html(new StringBuilder(), null);
        fail("SerializationException expected but not thrown");
    }

    @Test
    public void testHtmlEmptyAttributes() throws IOException, SerializationException {
        Attributes attributes = new Attributes();
        StringBuilder accumulator = new StringBuilder();
        attributes.html(accumulator, new Document.OutputSettings());
        assertEquals("", accumulator.toString());
    }

    @Test
    public void testEqualsNull() {
        Attributes attributes = new Attributes();
        assertEquals(attributes, null);
    }

    @Test
    public void testEqualsSameObject() {
        Attributes attributes = new Attributes();
        assertEquals(attributes, attributes);
    }

    @Test
    public void testEqualsSameAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");
        assertEquals(attributes1, attributes2);
    }

    @Test
    public void testEqualsDifferentAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        assertNotEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCodeNullAttributes() {
        Attributes attributes = new Attributes(null);
        assertEquals(0, attributes.hashCode());
    }

    @Test
    public void testHashCodeSameAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key", "value");
        Attributes attributes2 = new Attributes();
        attributes2.put("key", "value");
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testHashCodeDifferentAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }
}