package org.jsoup.nodes; // Import the Attributes class

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    

    // Test basic functionality

    @Test
    public void testGet() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key", "VALUE");
        assertEquals("VALUE", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPut() {
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.put("Key", "VALUE");
        assertEquals("VALUE", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key", true);
        assertFalse(attributes.get("key").isEmpty());
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testPutBooleanWithValue() {
        attributes.put("key", true);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<String> attributeKeys = new ArrayList<>();

        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            attributeKeys.add(attribute.getKey());
        }

        List<String> expected = Arrays.asList("key1", "key2");
        assertEquals(expected, attributeKeys);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        List<String> expected = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        assertEquals(expected, attributesList);
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        assertEquals(expected, dataset);
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();
        String expected = " key1=\"value1\" key2=\"value2\"";

        assertEquals(expected, html);
    }

    // Test edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyStringAsKey() {
        attributes.put("", "value");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBoundsIndex() {
        attributes.remove(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKey() {
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutDuplicateKeyIgnoreCase() {
        attributes.put("Key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveOnEmptyList() {
        Iterator<Attribute> iterator = attributes.iterator();
        iterator.remove();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNegativeSize() {
        attributes.checkCapacity(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyOfNullArray() {
        String[] orig = null;
        String[] copy = Attributes.copyOf(orig, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckCapacityBelowSize() {
        attributes.checkCapacity(attributes.size() - 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullAttributes() {
        String html = new Attributes().get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIgnoreCaseNullAttributes() {
        String html = new Attributes().getIgnoreCase(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKeyAndValue() {
        attributes.put(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutBooleanNullKey() {
        attributes.put(null, true);
    }

    @Test(expected = NullPointerException.class)
    public void testPutBooleanNullAttribute() {
        attributes.put(new Attribute(null, "value"), true);
    }

    @Test(expected = NullPointerException.class)
    public void testPutBooleanNullParent() {
        new Attribute("key", null).put(true);
    }

    @Test(expected = NullPointerException.class)
    public void testPutBooleanNullValue() {
        attributes.put("key", null);
        attributes.put("key", true);
        attributes.put("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testDatasetNullAttributes() {
        Object dataset = new Attributes().dataset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDatasetNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Map<String, String> dataset = attributes.dataset();
        dataset.put(null, "value");
    }

    
}