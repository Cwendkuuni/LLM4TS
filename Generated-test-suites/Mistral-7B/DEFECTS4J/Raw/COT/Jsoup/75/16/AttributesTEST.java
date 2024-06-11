package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
    }

    @Test
    public void testGet() {
        String value1 = attributes.get("key1");
        String value2 = attributes.get("key2");

        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
        Assert.assertEquals("", attributes.get("nonExistentKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        String value1 = attributes.getIgnoreCase("key1");
        String value2 = attributes.getIgnoreCase("key2");

        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
        Assert.assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullKey() {
        attributes.get(null);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        Assert.assertEquals(3, attributes.size());
        Assert.assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutIgnoreCase() {
        attributes.putIgnoreCase("Key3", "value3");
        Assert.assertEquals(3, attributes.size());
        Assert.assertEquals("value3", attributes.get("key3"));
        Assert.assertEquals("value3", attributes.get("Key3"));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testPutSameKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.get("booleanKey") == null);
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertEquals(1, attributes.size());

        attributes.put("booleanKey", false);
        Assert.assertFalse(attributes.hasKey("booleanKey"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.put("key3", "value3");
        Assert.assertEquals(3, attributes.size());
        attributes.remove("key3");
        Assert.assertEquals(2, attributes.size());
        Assert.assertFalse(attributes.hasKey("key3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidIndex() {
        attributes.remove(-1);
    }

    @Test
    public void testHasKey() {
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key4", "value4");

        attributes.addAll(attributes2);
        Assert.assertEquals(4, attributes.size());
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test
    public void testIterator() {
        List<Map.Entry<String, String>> actual = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );
        List<Map.Entry<String, String>> expected = attributes.asList();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        List<Map.Entry<String, String>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        );

        Assert.assertEquals(expected, dataset.entrySet());
    }

    @Test
    public void testHtml() {
        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\"");

        Assert.assertEquals(expected.toString(), attributes.html());
    }

    @Test
    public void testClone() {
        Attributes cloned = attributes.clone();
        Assert.assertEquals(attributes, cloned);
        Assert.assertEquals(attributes.size(), cloned.size());
        Assert.assertEquals(attributes.get("key1"), cloned.get("key1"));
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attributes attributes1 = new Attributes();
        throw new TestCase().assertThrows(CloneNotSupportedException.class, () -> attributes1.clone());
    }
}