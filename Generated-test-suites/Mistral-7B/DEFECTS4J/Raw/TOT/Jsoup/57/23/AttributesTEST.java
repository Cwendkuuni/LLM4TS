package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String expected = "value";
        String actual = attributes.get("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        String expected = "Value";
        String actual = attributes.getIgnoreCase("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethodWithNullAttributes() {
        Attributes attributes = new Attributes();

        String actual = attributes.get("key");

        assertNull(actual);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean expected = true;
        boolean actual = attributes.hasKey("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        boolean expected = true;
        boolean actual = attributes.hasKeyIgnoreCase("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethodWithNullAttributes() {
        Attributes attributes = new Attributes();

        boolean actual = attributes.hasKey("key");

        assertFalse(actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethodWithNullKey() {
        Attributes attributes = new Attributes();

        assertFalse(attributes.hasKeyIgnoreCase(null));
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");

        String expected = "value";
        String actual = attributes.get("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);

        boolean expected = true;
        boolean actual = attributes.hasKey("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethodWithFalseValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", false);

        boolean expected = false;
        boolean actual = attributes.hasKey("key");

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithNullValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", null);
    }

    @Test
    public void testPutMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String expected = "value2";
        String actual = attributes.get("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testPutMethodWithNewValueForExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String expected = "value2";
        String actual = attributes.get("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");

        String actual = attributes.get("key");

        assertNull(actual);
    }

    @Test
    public void testRemoveMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();

        attributes.remove("key");

        assertFalse(attributes.hasKey("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.remove(null);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        attributes.removeIgnoreCase("key");

        boolean expected = false;
        boolean actual = attributes.hasKey("key");

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveIgnoreCaseMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();

        attributes.removeIgnoreCase("key");

        assertFalse(attributes.hasKey("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseMethodWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.removeIgnoreCase(null);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int expected = 2;
        int actual = attributes.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        int actual = attributes.size();

        assertEquals(0, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes expected = new Attributes();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        Attributes attributes = new Attributes();
        attributes.addAll(attributes1);
        attributes.addAll(attributes2);

        Attributes actual = attributes;

        assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        Attributes expected = new Attributes();

        attributes.addAll(expected);

        Attributes actual = attributes;

        assertEquals(expected, actual);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expected = new ArrayList<>();
        expected.add(new Attribute("key1", "value1"));
        expected.add(new Attribute("key2", "value2"));

        List<Attribute> actual = attributes.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testAsListMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        List<Attribute> actual = attributes.asList();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("data-key2", "value2");

        Map<String, String> actual = attributes.dataset();

        assertEquals(expected, actual);
    }

    @Test
    public void testDatasetMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        Map<String, String> actual = attributes.dataset();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String expected = " key=\"value\"";
        String actual = attributes.html();

        assertEquals(expected, actual);
    }

    @Test
    public void testHtmlMethodWithEmptyAttributes() {
        Attributes attributes = new Attributes();

        String actual = attributes.html();

        assertEquals("", actual);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");

        int expected1 = attributes1.hashCode();
        int expected2 = attributes2.hashCode();

        int actual1 = attributes1.hashCode();
        int actual2 = attributes2.hashCode();

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }
}