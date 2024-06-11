package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

/**
 * Test class for Attributes class.
 */
public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        actual = attributes.getIgnoreCase("Key");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.get(null);
    }

    @Test
    public void testGetWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.get("");
        assertEquals("", actual);
    }

    @Test
    public void testGetWithUnsetKey() {
        Attributes attributes = new Attributes();

        String actual = attributes.get("key");
        assertEquals("", actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");

        String actual = attributes.get("key");
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put("key", "newValue");

        actual = attributes.get("key");
        expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, "value");

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();

        attributes.put("", "value");

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutWithUnsetKey() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");

        attributes.put("key", "newValue");

        String actual = attributes.get("key");
        String expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.getBoolean("key"));
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutBooleanWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, true);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutBooleanWithEmptyKey() {
        Attributes attributes = new Attributes();

        attributes.put("", true);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutBooleanWithUnsetKey() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);
        attributes.put("key", false);

        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");

        attributes.put(attribute);

        String actual = attributes.get(key);
        String expected = "value";
        assertEquals(expected, actual);

        attributes.put(new Attribute(key, "newValue"));

        actual = attributes.get(key);
        expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testPutAttributeWithNullAttribute() {
        Attributes attributes = new Attributes();

        attributes.put(null);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAttributeWithEmptyKeyAttribute() {
        Attributes attributes = new Attributes();

        Attribute attribute = new Attribute("", "value");

        attributes.put(attribute);

        assertEquals(0, attributes.size());
    }

    @Test
    public void testPutAttributeWithUnsetKeyAttribute() {
        Attributes attributes = new Attributes();
        String key = "key";
        Attribute attribute = new Attribute(key, "value");

        attributes.put(attribute);
        attributes.put(new Attribute(key, "newValue"));

        String actual = attributes.get(key);
        String expected = "newValue";
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");

        String actual = attributes.get("key");
        assertEquals("", actual);
    }

    @Test
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove(null);

        String actual = attributes.get("key");
        assertEquals("value", actual);
    }

    @Test
    public void testRemoveWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("");

        String actual = attributes.get("key");
        assertEquals("value", actual);
    }

    @Test
    public void testRemoveWithUnsetKey() {
        Attributes attributes = new Attributes();

        attributes.remove("key");

        assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        attributes.removeIgnoreCase("key");

        String actual = attributes.get("Key");
        assertEquals("value", actual);
    }

    @Test
    public void testRemoveIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        attributes.removeIgnoreCase(null);

        String actual = attributes.get("Key");
        assertEquals("value", actual);
    }

    @Test
    public void testRemoveIgnoreCaseWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        attributes.removeIgnoreCase("");

        String actual = attributes.get("Key");
        assertEquals("value", actual);
    }

    @Test
    public void testRemoveIgnoreCaseWithUnsetKey() {
        Attributes attributes = new Attributes();

        attributes.removeIgnoreCase("Key");

        assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase(null));
    }

    @Test
    public void testHasKeyIgnoreCaseWithEmptyKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase(""));
    }

    @Test
    public void testHasKeyIgnoreCaseWithUnsetKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase("Key"));
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

        attributes1.addAll(attributes2);

        assertEquals(2, attributes1.size());

        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testAddAllWithEmptySet() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.addAll(attributes2);

        assertEquals(0, attributes1.size());
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();

        assertEquals(2, attributesList.size());

        Attribute attribute1 = attributesList.get(0);
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());

        Attribute attribute2 = attributesList.get(1);
        assertEquals("key2", attribute2.getKey());
        assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());

        String actual = dataset.get("data-key1");
        String expected = "value1";
        assertEquals(expected, actual);

        actual = dataset.get("data-key2");
        expected = "value2";
        assertEquals(expected, actual);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String actual = attributes.html();
        String expected = " key=\"value\"";
        assertEquals(expected, actual);
    }

    @Test
    public void testHtmlWithEmptySet() {
        Attributes attributes = new Attributes();

        String actual = attributes.html();
        String expected = "";
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());

        assertTrue(attributes.hasKey("key1"));
        assertTrue(clonedAttributes.hasKey("key1"));
        assertFalse(attributes.equals(clonedAttributes));
    }
}