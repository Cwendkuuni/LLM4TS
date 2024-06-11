package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testSize() {
        Attributes attrs = new Attributes();
        assertEquals(0, attrs.size());

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.put("key3", "value3");
        attrs.put("key1", "value1_updated");

        assertEquals(3, attrs.size());
    }

    @Test
    public void testPutGet() {
        Attributes attrs = new Attributes();

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertEquals("value1", attrs.get("key1"));
        assertEquals("value2", attrs.get("key2"));
        assertNull(attrs.get("key3"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attrs = new Attributes();

        attrs.putIgnoreCase("key1", "Value1");
        attrs.putIgnoreCase("key2", "Value2");

        assertEquals("Value1", attrs.getIgnoreCase("key1"));
        assertEquals("Value2", attrs.getIgnoreCase("key2"));
        assertNull(attrs.getIgnoreCase("key3"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attrs = new Attributes();

        attrs.put("key1", true);
        attrs.put("key2", false);

        assertTrue(attrs.getBoolean("key1"));
        assertFalse(attrs.getBoolean("key2"));
        assertNull(attrs.getBoolean("key3"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attrs = new Attributes();

        Attribute attribute = new Attribute("key1", "value1");
        attrs.put(attribute);

        assertEquals("value1", attrs.get("key1"));
        assertEquals(attribute, attrs.get(0));
    }

    @Test
    public void testRemove() {
        Attributes attrs = new Attributes();

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");
        attrs.put("key3", "value3");

        attrs.remove("key1");
        attrs.remove("key2");

        assertEquals(1, attrs.size());
        assertNull(attrs.get("key1"));
        assertNull(attrs.get("key2"));
        assertEquals("value3", attrs.get("key3"));
    }

    @Test
    public void testHasKey() {
        Attributes attrs = new Attributes();

        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        assertTrue(attrs.hasKey("key1"));
        assertTrue(attrs.hasKey("key2"));
        assertFalse(attrs.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attrs = new Attributes();

        attrs.putIgnoreCase("key1", "Value1");
        attrs.putIgnoreCase("key2", "Value2");

        assertTrue(attrs.hasKeyIgnoreCase("key1"));
        assertTrue(attrs.hasKeyIgnoreCase("key2"));
        assertFalse(attrs.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testAddAll() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");
        attrs1.put("key2", "value2");

        Attributes attrs2 = new Attributes();
        attrs2.put("key3", "value3");
        attrs2.put("key4", "value4");

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value2");
        expectedAttributes.put("key3", "value3");
        expectedAttributes.put("key4", "value4");

        Attributes attrs = new Attributes();
        attrs.addAll(attrs1);
        attrs.addAll(attrs2);

        assertEquals(expectedAttributes.size(), attrs.size());
        assertTrue(attrs.containsAll(expectedAttributes));
    }

    @Test
    public void testDataset() {
        Attributes attrs = new Attributes();
        attrs.put("data-key1", "value1");
        attrs.put("data-key2", "value2");

        Map<String, String> dataset = attrs.dataset();

        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        StringBuilder actualHtml = new StringBuilder();
        attrs.html(actualHtml, null);

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("Attributes{size=2, keys=[key1, key2], vals=[value1, value2]}");

        StringBuilder actualToString = new StringBuilder();
        actualToString.append(attrs.toString());

        assertEquals(expectedToString.toString(), actualToString.toString());
    }

    @Test
    public void testEquals() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");
        attrs1.put("key2", "value2");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");
        attrs2.put("key2", "value2");

        Attributes attrs3 = new Attributes();
        attrs3.put("key1", "value1_updated");
        attrs3.put("key2", "value2");

        Attributes attrs4 = new Attributes();
        attrs4.put("key1", "value1");
        attrs4.put("key3", "value3");

        assertTrue(attrs1.equals(attrs1));
        assertTrue(attrs1.equals(attrs2));
        assertFalse(attrs1.equals(attrs3));
        assertFalse(attrs1.equals(attrs4));
    }

    @Test
    public void testHashCode() {
        Attributes attrs1 = new Attributes();
        attrs1.put("key1", "value1");
        attrs1.put("key2", "value2");

        Attributes attrs2 = new Attributes();
        attrs2.put("key1", "value1");
        attrs2.put("key2", "value2");

        Attributes attrs3 = new Attributes();
        attrs3.put("key1", "value1_updated");
        attrs3.put("key2", "value2");

        Attributes attrs4 = new Attributes();
        attrs4.put("key1", "value1");
        attrs4.put("key3", "value3");

        int attrs1HashCode = attrs1.hashCode();
        int attrs2HashCode = attrs2.hashCode();
        int attrs3HashCode = attrs3.hashCode();
        int attrs4HashCode = attrs4.hashCode();

        assertEquals(attrs1HashCode, attrs2.hashCode());
        assertNotEquals(attrs1HashCode, attrs3.hashCode());
        assertNotEquals(attrs1HashCode, attrs4.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        Attributes clonedAttributes = attrs.clone();

        assertEquals(attrs.size(), clonedAttributes.size());
        assertTrue(attrs.equals(clonedAttributes));
    }

    @Test
    public void testNormalize() {
        Attributes attrs = new Attributes();
        attrs.put("KEY1", "value1");
        attrs.put("KEY2", "value2");

        attrs.normalize();

        assertEquals("key1", attrs.get("KEY1"));
        assertEquals("value1", attrs.get("key1"));
        assertEquals("key2", attrs.get("KEY2"));
        assertEquals("value2", attrs.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attrs = new Attributes();
        attrs.put("key1", "value1");
        attrs.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new SimpleEntry<>("key1", "value1"),
                new SimpleEntry<>("key2", "value2")
        );

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attrs) {
            actualList.add(entry);
        }

        assertEquals(expectedList, actualList);
    }
}