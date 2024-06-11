package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @After
    public void tearDown() {
        attributes = null;
    }

    @Test
    public void testGetEmpty() {
        assertNull(attributes.get(""));
    }

    @Test
    public void testGetKeyEmpty() {
        assertNull(attributes.get(""));
    }

    @Test
    public void testGetIgnoreCaseEmpty() {
        assertNull(attributes.getIgnoreCase(""));
    }

    @Test
    public void testGetNull() {
        assertNull(attributes.get(null));
    }

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("key", false);
        attributes.put("key", true);
        assertFalse(attributes.getBoolean("key", false));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("key", true);
        attributes.put("key", false);
        assertTrue(attributes.getBoolean("key", true));
    }

    @Test
    public void testPutNullValue() {
        attributes.put("key", null);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testPutSameKeyDifferentValue() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test
    public void testPutSameKeySameValue() {
        attributes.put("key", "value");
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutAttribute() {
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutDataAttribute() {
        attributes.put(new Attribute("data-key", "value"));
        assertEquals("value", attributes.getIgnoreCase("data-key"));
    }

    @Test
    public void testRemove() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key", "value");
        attributes.removeIgnoreCase("key");
        assertNull(attributes.get("key"));
        assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveUndefinedKey() {
        assertNull(attributes.remove("key"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("Other"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllEmpty() {
        Attributes empty = new Attributes();
        attributes.addAll(empty);
        assertNull(attributes.get("key"));
    }

    @Test
    public void testAddAllNonEmpty() {
        attributes.put("key1", "value1");
        Attributes nonEmpty = new Attributes();
        nonEmpty.put("key2", "value2");
        attributes.addAll(nonEmpty);
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        Iterator<Attribute> iterator = attributesList.iterator();
        assertTrue(iterator.hasNext());
        Attribute attribute = iterator.next();
        assertSame("value1", attribute.getValue());
        assertTrue(iterator.hasNext());
        attribute = iterator.next();
        assertSame("value2", attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDatasetEmpty() {
        Map<String, String> dataset = attributes.dataset();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testDatasetNonEmpty() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtmlEmpty() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testHtmlNonEmpty() {
        attributes.put("key", "value");
        assertEquals(" key=\"value\"", attributes.html());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(attributes.equals(attributes));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(attributes.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(attributes.equals(new Object()));
    }

    @Test
    public void testEqualsSameAttributes() {
        Attributes same = new Attributes();
        same.put("key", "value");
        assertTrue(attributes.equals(same));
    }

    @Test
    public void testEqualsDifferentAttributes() {
        Attributes different = new Attributes();
        different.put("key1", "value1");
        assertFalse(attributes.equals(different));
    }

    @Test
    public void testCloneEmpty() {
        Attributes empty = new Attributes();
        Attributes clone = empty.clone();
        assertTrue(empty.equals(clone));
    }

    @Test
    public void testCloneNonEmpty() {
        attributes.put("key", "value");
        Attributes clone = attributes.clone();
        assertTrue(attributes.equals(clone));
    }
}