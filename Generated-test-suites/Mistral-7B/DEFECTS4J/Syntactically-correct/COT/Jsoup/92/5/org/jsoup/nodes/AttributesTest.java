package org.jsoup.nodes; // make sure to import all necessary classes for the test

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private static final String TEST_KEY = "testKey";
    private static final String TEST_VALUE = "testValue";
    private static final String TEST_EMPTY_VALUE = "";
    private static final String TEST_BOOL_VALUE = "true";
    private static final String TEST_BOOL_FALSE_VALUE = "false";
    private static final String TEST_DATA_KEY = "data-testKey";
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @After
    public void tearDown() {
        attributes = null; // clean up for the next test
    }

    @Test
    public void testGetEmpty() {
        assertEquals(EMPTY_STRING, attributes.get(EMPTY_STRING));
    }

    @Test
    public void testGetExists() {
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
    }

    @Test
    public void testGetIgnoreCaseExists() {
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_key.toUpperCase()));
    }

    @Test
    public void testGetIgnoreCaseDoesNotExist() {
        assertEquals(EMPTY_STRING, attributes.getIgnoreCase("testKeyIgnoreCase"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.get(null);
    }

    @Test
    public void testPut() {
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put(TEST_KEY.toUpperCase(), TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.getIgnoreCase(TEST_KEY));
    }

    @Test
    public void testPutDuplicateKey() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
        assertEquals(TEST_VALUE.length(), attributes.size());
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put(TEST_KEY, TEST_BOOL_VALUE);
        assertTrue(attributes.hasKey(TEST_KEY));
        assertEquals(EMPTY_STRING, attributes.get(TEST_KEY));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put(TEST_KEY, TEST_BOOL_FALSE_VALUE);
        assertFalse(attributes.hasKey(TEST_KEY));
    }

    @Test
    public void testPutIgnoreCaseBooleanTrue() {
        attributes.putIgnoreCase(TEST_KEY, TEST_BOOL_VALUE);
        assertTrue(attributes.hasKeyIgnoreCase(TEST_KEY));
        assertEquals(EMPTY_STRING, attributes.getIgnoreCase(TEST_KEY));
    }

    @Test
    public void testPutIgnoreCaseBooleanFalse() {
        attributes.putIgnoreCase(TEST_KEY, TEST_BOOL_FALSE_VALUE);
        assertFalse(attributes.hasKeyIgnoreCase(TEST_KEY));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        attributes.put(null, TEST_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        attributes.put(TEST_KEY, null);
    }

    @Test
    public void testRemove() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.remove(TEST_KEY);
        assertFalse(attributes.hasKey(TEST_KEY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidIndex() {
        attributes.remove(-1);
    }

    @Test
    public void testHasKey() {
        attributes.put(TEST_KEY, TEST_VALUE);
        assertTrue(attributes.hasKey(TEST_KEY));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put(TEST_KEY, TEST_VALUE);
        assertTrue(attributes.hasKeyIgnoreCase(TEST_key.toUpperCase()));
    }

    @Test
    public void testHasKeyIgnoreCaseDoesNotExist() {
        assertFalse(attributes.hasKeyIgnoreCase("testKeyIgnoreCase"));
    }

    @Test
    public void testSize() {
        assertEquals(0, attributes.size());
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(1, attributes.size());
        attributes.put(TEST_KEY, TEST_VALUE);
        assertEquals(1, attributes.size()); // duplicate key does not change size
    }

    @Test
    public void testAddAllEmpty() {
        Attributes emptyAttributes = new Attributes();
        attributes.addAll(emptyAttributes);
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put(TEST_KEY, TEST_VALUE);
        attributes.addAll(otherAttributes);
        assertEquals(TEST_VALUE, attributes.get(TEST_KEY));
    }

    @Test
    public void testToString() {
        attributes.put(TEST_KEY, TEST_VALUE);
        String attributesHtml = attributes.toString();
        assertTrue(attributesHtml.startsWith(TEST_VALUE));
        assertTrue(attributesHtml.endsWith(">"));
    }

    @Test
    public void testEquals() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put(TEST_KEY, TEST_VALUE);
        Attributes clonedAttributes = (Attributes) attributes.clone();
        assertTrue(attributes.equals(clonedAttributes));
        assertTrue(attributes.equals(otherAttributes));

        Attributes differentAttributes = new Attributes();
        differentAttributes.put(TEST_KEY, TEST_VALUE);
        differentAttributes.put("newKey", "newValue");
        assertFalse(attributes.equals(differentAttributes));
    }

    @Test
    public void testHashCode() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put(TEST_KEY, TEST_VALUE);
        Attributes clonedAttributes = (Attributes) attributes.clone();
        assertEquals(attributes.hashCode(), clonedAttributes.hashCode());
        assertEquals(attributes.hashCode(), otherAttributes.hashCode());

        Attributes differentAttributes = new Attributes();
        differentAttributes.put(TEST_KEY, TEST_VALUE);
        differentAttributes.put("newKey", "newValue");
        assertNotEquals(attributes.hashCode(), differentAttributes.hashCode());
    }

    @Test
    public void testDataset() {
        attributes.put(TEST_DATA_KEY, TEST_VALUE);
        Map<String, String> dataset = attributes.dataset();
        assertEquals(TEST_VALUE, dataset.get(TEST_DATA_KEY));
    }

    @Test
    public void testGetDataKey() {
        assertEquals(TEST_DATA_KEY, attributes.getDataKey(TEST_KEY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataKeyNullKey() {
        attributes.getDataKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataKeyEmptyKey() {
        attributes.put(EMPTY_STRING, TEST_VALUE);
        attributes.getDataKey(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataKeyEmptyValue() {
        attributes.put(TEST_KEY, EMPTY_STRING);
        attributes.getDataKey(TEST_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataKeyDuplicateKey() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.getDataKey(TEST_KEY);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataKeyRemovedKey() {
        attributes.put(TEST_KEY, TEST_VALUE);
        attributes.remove(TEST_KEY);
        attributes.getDataKey(TEST_KEY);
    }

    @Test
    public void testNormalize() {
        attributes.put("tEstKey", TEST_VALUE);
        attributes.normalize();
        assertEquals("testKey", attributes.get("testKey"));
    }
}