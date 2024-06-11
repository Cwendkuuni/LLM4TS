package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class AttributesTest {

    // Basic test cases for each method in the Attributes class

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
        Attributes newAttributes = new Attributes();
        newAttributes.put("key", "newValue");
        attributes.putAll(newAttributes);
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);
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
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKeyIgnoreCase("key"));
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
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
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        assertEquals(2, attributes3.size());
        assertEquals("value1", attributes3.get("key1"));
        assertEquals("value2", attributes3.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = (List<Attribute>) attributes.asList();
        Iterator<Attribute> iterator = attributes.iterator();
        assertEquals(attributesList.get(0), iterator.next());
        assertEquals(attributesList.get(1), iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = (List<Attribute>) attributes.asList();
        assertEquals(2, attributesList.size());
        assertEquals("value1", attributesList.get(0).getValue());
        assertEquals("value2", attributesList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "dataValue1");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("dataValue1", dataset.get("key1"));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("<key>value</key>", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        assertTrue(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        assertEquals(attributes.hashCode(), attributes.hashCode());
        assertNotEquals(attributes.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals("value1", clonedAttributes.get("key1"));
    }

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        Attributes attributes = new Attributes();
        attributes.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test
    public void testPutNullAttributes() {
        Attributes attributes = new Attributes();
        Attributes nullAttributes = null;
        try {
            attributes.putAll(nullAttributes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllDuplicateKeys() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value2");
        try {
            attributes1.putAll(attributes2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllNullAttributes() {
        Attributes attributes = new Attributes();
        Attributes nullAttributes = null;
        try {
            attributes.putAll(nullAttributes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllEmptyAttributes() {
        Attributes attributes = new Attributes();
        Attributes emptyAttributes = new Attributes();
        try {
            attributes.putAll(emptyAttributes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRemoveNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
        // Expected empty
    }

    @Test
    public void testRemoveIgnoreCaseNullKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
        // Expected empty
    }

    @Test
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
        // Expected empty
    }

    @Test
    public void testRemoveNonExistentIgnoreCaseKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase("nonExistentKey");
        // Expected empty
    }

    @Test
    public void testRemoveAll() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.clear();
        // Expected empty
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentIgnoreCaseKeyEmptyAttributes() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase("nonExistentKey");
        // Expected empty
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
}