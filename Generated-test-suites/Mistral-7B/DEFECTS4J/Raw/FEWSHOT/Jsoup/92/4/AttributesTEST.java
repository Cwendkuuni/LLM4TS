package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testInitialState() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        assertNull(attributes.get("foo"));
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testAddBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveNonExistentAttribute() {
        Attributes attributes = new Attributes();
        attributes.remove("key1");
        assertEquals(0, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("KEY1", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutIgnoreCaseDuplicate() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("KEY1", "value2");
        assertEquals(1, attributes.size());
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testPutAndGetMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutAndGetSameAttributeMultipleTimes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        attributes.put("key1", "value3");
        assertEquals(1, attributes.size());
        assertEquals("value3", attributes.get("key1"));
    }

    @Test
    public void testPutAndGetBooleanAttributeMultipleTimes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key1", false);
        attributes.put("key1", true);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAndGetBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertEquals(1, attributes.size());
        assertSame(attributes, attributes.put("key1", true));
        assertNull(attributes.get("key1"));
    }

    @Test
    public void testPutAndGetBooleanAttributeWithFalseValue() {
        Attributes attributes = new Attributes();
        attributes.put("key1", false);
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
        fail();
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        fail();
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"));

        List<Map.Entry<String, String>> actual = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actual.add(entry);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expected = Arrays.asList(
                new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes));

        List<Attribute> actual = attributes.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        Map<String, String> actual = attributes.dataset();

        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder expected = new StringBuilder();
        attributes.html(expected, new Document.OutputSettings());
        StringBuilder actual = new StringBuilder();
        attributes.html(actual, new Document.OutputSettings());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");

        Attributes attributes5 = new Attributes();
        attributes5.put("key1", "value1");
        attributes5.put("key2", "value2");

        assertEquals(attributes1, attributes1);
        assertEquals(attributes1, attributes5);
        assertNotEquals(attributes1, attributes2);
        assertNotEquals(attributes1, attributes3);
        assertNotEquals(attributes1, attributes4);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();

        assertEquals(1, attributes.size());
        assertEquals(1, cloned.size());
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value1", cloned.get("key1"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");

        attributes.normalize();

        assertEquals("value1", attributes.get("key1"));
    }
}