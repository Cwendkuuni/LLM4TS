package org.jsoup.nodes; // Make sure the test package is the same as the class package

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("", attributes.get(null));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testGetIgnoreCaseWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals("", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testGetIgnoreCaseWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals("", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("Key"));
    }

    @Test
    public void testGetWithEmptyKey() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get(EmptyString));
        assertEquals("", attributes.getIgnoreCase(EmptyString));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put(EmptyString, "value");
        assertEquals("value", attributes.get(EmptyString));
        assertEquals("value", attributes.getIgnoreCase(EmptyString));
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutWithExistingKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("Key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertEquals(null, attributes.get("key"));
        assertEquals(null, attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertEquals("", attributes.get("key"));
        assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutWithBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testPutWithBooleanValueAndRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testPutWithBooleanValueAndReplace() {
        Attributes attributes = new Attributes();
        attributes.put("key", false);
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testRemoveWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.remove(EmptyString);
        assertFalse(attributes.hasKey(EmptyString));
        assertFalse(attributes.hasKeyIgnoreCase(EmptyString));
    }

    @Test
    public void testRemoveWithIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testRemoveWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testHasKeyWithEmptyKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey(EmptyString));
        assertFalse(attributes.hasKeyIgnoreCase(EmptyString));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKeyIgnoreCaseWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
    }

    @Test
    public void testHasKeyIgnoreCaseWithNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
        assertTrue(attributes.hasKey("key"));
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
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
        Attributes result = new Attributes();

        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(2, result.size());
        assertTrue(result.hasKey("key1"));
        assertTrue(result.hasKey("key2"));
    }

    @Test
    public void testAddAllWithEmptyAttributes() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes result = new Attributes();

        result.addAll(attributes1);
        result.addAll(attributes2);

        assertEquals(0, result.size());
        assertFalse(result.hasKey("key1"));
        assertFalse(result.hasKey("key2"));
    }

    @Test
    public void testAddAllWithSelf() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        try {
            attributes.addAll(attributes);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
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
                new Attribute("key2", "value2", attributes)
        );

        List<Attribute> actual = attributes.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        Map<String, String> actual = attributes.dataset();

        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expected = " key1=\"value1\" key2=\"value2\"";
        String actual = attributes.html();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");

        int expected1 = 31 * (31 * 2 + Arrays.hashCode(new String[]{"key1"})) + Arrays.hashCode(new String[]{"value1"});
        int expected2 = expected1;
        int expected3 = 31 * (31 * 2 + Arrays.hashCode(new String[]{"key2"})) + Arrays.hashCode(new String[]{"value2"});

        int actual1 = attributes1.hashCode();
        int actual2 = attributes2.hashCode();
        int actual3 = attributes3.hashCode();

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertNotEquals(expected1, actual3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();

        assertEquals(attributes, cloned);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.normalize();

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
    }

    
}