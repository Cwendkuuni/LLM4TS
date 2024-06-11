package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testAddIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Test", "value");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testAddAttributeWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");
        attributes.put("test", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("test"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.hasKey("test"));
        assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("test");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        attributes.removeIgnoreCase("test");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("test"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveAttributeWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove(null);
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveAttributeWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        attributes.remove("");
        assertEquals(1, attributes.size());
        assertEquals("value", attributes.get("test"));
        assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testGetAttributeWhenNotExists() {
        Attributes attributes = new Attributes();
        assertNull(attributes.get("test"));
    }

    @Test
    public void testGetAttributeWhenExists() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testGetAttributeIgnoreCaseWhenExists() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertEquals("value", attributes.getIgnoreCase("test"));
    }

    @Test
    public void testGetAttributeIgnoreCaseWhenNotExists() {
        Attributes attributes = new Attributes();
        assertNull(attributes.getIgnoreCase("test"));
    }

    @Test
    public void testContains() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        assertTrue(attributes.containsKey("test"));
        assertFalse(attributes.containsKey("other"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "value");
        assertTrue(attributes.containsKeyIgnoreCase("test"));
        assertFalse(attributes.containsKeyIgnoreCase("other"));
    }

    @Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.putAll(Arrays.asList(attributes1, attributes2));
        assertEquals(2, attributes3.size());
        assertEquals("value1", attributes3.get("test1"));
        assertEquals("value2", attributes3.get("test2"));
    }

    @Test
    public void testPutAllWithDuplicateKeys() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.putAll(Arrays.asList(attributes1, attributes2));
        assertEquals(1, attributes3.size());
        assertEquals("value2", attributes3.get("test"));
    }

    @Test
    public void testPutAllWithNullValues() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", null);
        Attributes attributes3 = new Attributes();
        attributes3.putAll(Arrays.asList(attributes1, attributes2));
        assertEquals(2, attributes3.size());
        assertNull(attributes3.get("test2"));
    }

    @Test
    public void testPutAllWithEmptyValues() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "");
        Attributes attributes3 = new Attributes();
        attributes3.putAll(Arrays.asList(attributes1, attributes2));
        assertEquals(2, attributes3.size());
        assertEquals("", attributes3.get("test2"));
    }

    @Test
    public void testPutAndGetBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertTrue(attributes.get("test") == null);
        attributes.put("test", false);
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutAndGetBooleanAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("TEST", true);
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertTrue(attributes.getIgnoreCase("test") == null);
        attributes.putIgnoreCase("TEST", false);
        assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testPutAndRemoveBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertTrue(attributes.get("test") == null);
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutAndRemoveBooleanAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("TEST", true);
        assertTrue(attributes.hasKeyIgnoreCase("test"));
        assertTrue(attributes.getIgnoreCase("test") == null);
        attributes.removeIgnoreCase("TEST");
        assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @Test
    public void testPutAndRemoveBooleanAttributeWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        assertTrue(attributes.hasKey("test"));
        assertTrue(attributes.get("test") == null);
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutAndRemoveBooleanAttributeWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("test", "");
        assertTrue(attributes.hasKey("test"));
        assertTrue(attributes.get("test").isEmpty());
        attributes.remove("test");
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(1, clonedAttributes.size());
        assertEquals("value1", clonedAttributes.get("test1"));
        assertTrue(clonedAttributes.hasKey("test1"));
    }

    @Test
    public void testCloneWithDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test1", "value2");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(1, clonedAttributes.size());
        assertEquals("value2", clonedAttributes.get("test1"));
    }

    @Test
    public void testCloneWithNullValues() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(1, clonedAttributes.size());
        assertNull(clonedAttributes.get("test1"));
    }

    @Test
    public void testCloneWithEmptyValues() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(1, clonedAttributes.size());
        assertEquals("", clonedAttributes.get("test1"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-test1"));
        assertEquals("value2", dataset.get("data-test2"));
    }

    @Test
    public void testDatasetWhenEmpty() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(0, dataset.size());
    }
}