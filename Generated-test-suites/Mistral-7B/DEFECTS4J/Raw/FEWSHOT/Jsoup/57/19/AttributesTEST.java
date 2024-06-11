package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertEquals(2, attrs.size());
    }

    @Test
    public void testGet() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        assertEquals("value1", attrs.get("key1"));
        assertEquals("", attrs.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "value1");

        assertEquals("value1", attrs.getIgnoreCase("key1"));
        assertEquals("value1", attrs.getIgnoreCase("Key1"));
        assertEquals("", attrs.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        assertEquals("value1", attrs.get("key1"));
        attrs.put("key1", "newValue1");
        assertEquals("newValue1", attrs.get("key1"));

        attrs.put("key2", "value2");
        assertEquals("value2", attrs.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attrs = new Attributes();
        attrs.put("key1", true);

        assertTrue(attrs.hasKey("key1"));
        assertFalse(attrs.hasKey("key2"));
        assertNull(attrs.get("key2"));
        assertEquals("", attrs.get("key1"));
    }

    @Test
    public void testPutNull() {
        Attributes attrs = new Attributes();
        attrs.put(null);

        assertNull(attrs.get(null));
        assertFalse(attrs.hasKey(null));
    }

    @Test
    public void testPutNullValue() {
        Attributes attrs = new Attributes();
        attrs.put("key1", null);

        assertEquals(null, attrs.get("key1"));
        assertFalse(attrs.hasKey("key1"));
    }

    @Test
    public void testPutDuplicateKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key1", "value2");

        assertEquals("value2", attrs.get("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();
        Attribute attr = new Attribute("key1", "value1");
        attrs.put(attr);

        assertEquals("value1", attrs.get("key1"));
        assertEquals(attr, attrs.iterator().next());
    }

    @Test
    public void testPutAttributeNull() {
        Attributes attrs = new Attributes();
        Attribute attr = new Attribute(null, "value1");

        try {
            attrs.put(attr);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertNull(attrs.get(null));
    }

    @Test
    public void testPutAttributeNullValue() {
        Attributes attrs = new Attributes();
        Attribute attr = new Attribute(null, null);

        attrs.put(attr);
        assertNull(attrs.get(null));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        attrs.remove("key1");
        assertFalse(attrs.hasKey("key1"));
        assertNull(attrs.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "value1");

        attrs.removeIgnoreCase("key1");
        assertFalse(attrs.hasKey("key1"));
        assertFalse(attrs.hasKey("Key1"));
        assertNull(attrs.get("key1"));
        assertNull(attrs.get("Key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        assertTrue(attrs.hasKey("key1"));
        assertFalse(attrs.hasKey("key2"));
        assertFalse(attrs.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testHasKeyIgnoreCaseEmptyAttributes() {
        Attributes attrs = new Attributes();

        assertFalse(attrs.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyIgnoreCaseSingleMatch() {
        Attributes attrs = new Attributes();
        attrs.put("Key1", "value1");

        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("Key1"));
    }

    @Test
    public void testHasKeyIgnoreCaseMultipleMatches() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("Key2", "value2");

        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("Key1"));
        assertTrue(attrs.hasKeyIgnoreCase("Key2"));
    }

    @Test
    public void testHashCodeEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");

        assertEquals(attrs1.hashCode(), attrs2.hashCode());
        assertTrue(attrs1.equals(attrs2));
    }

    @Test
    public void testHashCodeNotEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");

        Attributes attrs2 = new Attributes();
        attrs2.put("key2", "value2");

        assertNotEquals(attrs1.hashCode(), attrs2.hashCode());
        assertFalse(attrs1.equals(attrs2));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        Attributes cloned = attrs.clone();

        assertEquals(attrs.size(), cloned.size());
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            String value = entry.getValue();
            String clonedValue = cloned.get(entry.getKey());
            assertEquals(value, clonedValue);
        }
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");
        attrs.put("key2", "value2");

        Map<String, String> dataset = attrs.dataset();

        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertNull(dataset.get("key2"));
    }

    @Test
    public void testDatasetEmptyAttributes() {
        Attributes attrs = new Attributes();

        Map<String, String> dataset = attrs.dataset();

        assertEmpty(dataset);
    }

    @Test
    public void testDatasetSingleDataAttribute() {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");

        Map<String, String> dataset = attrs.dataset();

        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\"");

        StringBuilder actual = new StringBuilder();
        attrs.html(actual, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testHtmlEmptyAttributes() throws IOException {
        Attributes attrs = new Attributes();

        StringBuilder expected = new StringBuilder();

        StringBuilder actual = new StringBuilder();
        attrs.html(actual, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testHtmlWithDataAttributes() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");

        StringBuilder expected = new StringBuilder();
        expected.append(" data-key1=\"value1\"");

        StringBuilder actual = new StringBuilder();
        attrs.html(actual, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\"");

        StringBuilder actual = new StringBuilder();
        actual.append(attrs.toString());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIterator() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        List<Attribute> attributesList = attrs.asList();

        int i = 0;
        for (Attribute attribute : attrs) {
            assertEquals(attributesList.get(i++).getValue(), attribute.getValue());
        }
    }

    @Test
    public void testAsListEmptyAttributes() {
        Attributes attrs = new Attributes();

        List<Attribute> attributesList = attrs.asList();

        assertEmpty(attributesList);
    }

    @Test
    public void testAsListSingleAttribute() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");

        List<Attribute> attributesList = attrs.asList();

        assertEquals(1, attributesList.size());
        Attribute attribute = attributesList.get(0);
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testAsListMultipleAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        List<Attribute> attributesList = attrs.asList();

        assertEquals(2, attributesList.size());
        for (int i = 0; i < attributesList.size(); i++) {
            Attribute attribute = attributesList.get(i);
            assertEquals(i + 1, attribute.getKey().charAt(0) == 'k' ? i + 1 : -i);
            assertEquals("value" + (i + 1), attribute.getValue());
        }
    }

    private void assertEmpty(Map<String, String> map) {
        assertTrue(map.isEmpty());
    }

}