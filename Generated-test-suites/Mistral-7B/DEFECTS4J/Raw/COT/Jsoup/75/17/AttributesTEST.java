package org.jsoup.nodes; // adjust the package name if necessary

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {
    // Test cases for basic functionality

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "VALUE");
        String actual = attributes.getIgnoreCase("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String actual = attributes.get("key");
        String expected = "value";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "VALUE");
        String actual = attributes.get("key");
        String expected = "VALUE";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.get("key").isEmpty());
    }

    @Test
    public void testPutMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutIgnoreCaseMethodWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value1");
        attributes.put("key", "value2");
        String actual = attributes.get("key");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean actual = attributes.hasKey("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean actual = attributes.hasKeyIgnoreCase("key");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKey("key");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethodWithNonExistingKey() {
        Attributes attributes = new Attributes();
        boolean actual = attributes.hasKeyIgnoreCase("Key");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int actual = attributes.size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        int actualSize = attributes1.size();
        int expectedSize = 3;
        Assert.assertEquals(expectedSize, actualSize);

        String actualKey1 = attributes1.get("key1");
        String expectedKey1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);

        String actualKey2 = attributes1.get("key2");
        String expectedKey2 = "value2";
        Assert.assertEquals(expectedKey2, actualKey2);

        String actualKey3 = attributes1.get("key3");
        String expectedKey3 = "value3";
        Assert.assertEquals(expectedKey3, actualKey3);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1"),
                new Attribute("key2", "value2")
        );

        List<Attribute> actualList = attributes.asList();

        Assert.assertEquals(expectedList, actualList);
    }

    // Test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutBooleanMethodWithNonBooleanValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "nonBooleanValue");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveMethodWithNonExistingIndex() {
        Attributes attributes = new Attributes();
        attributes.remove(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseMethodWithDuplicateKeys() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value1");
        attributes.putIgnoreCase("key", "value2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseMethodWithEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithLongKey() {
        Attributes attributes = new Attributes();
        String longKey = new String(new char[256]);
        attributes.put(longKey, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseMethodWithLongKey() {
        Attributes attributes = new Attributes();
        String longKey = new String(new char[256]);
        attributes.putIgnoreCase(longKey, "value");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveMethodWithExistingIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethodWithUnmodifiableList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> list = attributes.asList();

        try {
            list.remove(0);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDatasetMethodWithNullAttributes() {
        Attributes attributes = null;
        try {
            attributes.dataset();
        } catch (NullPointerException e) {
            // expected
        }
    }
}