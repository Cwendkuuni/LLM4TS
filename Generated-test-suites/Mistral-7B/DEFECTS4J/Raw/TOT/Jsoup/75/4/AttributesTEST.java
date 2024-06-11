package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGetEmptyAttributes() {
        Assert.assertEquals("", attributes.get(""));
        Assert.assertNull(attributes.get("notExistentKey"));
        Assert.assertFalse(attributes.hasKey(""));
        Assert.assertFalse(attributes.hasKey("notExistentKey"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testSetGetAttributes() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("KEY1"));

        attributes.put("key2", "value2");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertEquals(2, attributes.size());

        attributes.put("key1", "updatedValue1");
        Assert.assertEquals("updatedValue1", attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("key2")); // a key with the same name was overwritten
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testSetBooleanAttribute() {
        attributes.put("boolKey", true);
        Assert.assertNull(attributes.get("boolKey"));
        Assert.assertTrue(attributes.hasKey("boolKey"));
        Assert.assertEquals(1, attributes.size());

        attributes.put("boolKey", false);
        Assert.assertFalse(attributes.hasKey("boolKey"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveAttribute() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertEquals(1, attributes.size());

        attributes.remove("key1");
        Assert.assertNull(attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertEquals(1, attributes.size());

        attributes.removeIgnoreCase("key1");
        Assert.assertNull(attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAttribute() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
    }

    @Test
    public void testPutAllAttributes() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");

        attributes.putAll(attributes2);

        Assert.assertEquals("value3", attributes.get("key3"));
        Assert.assertTrue(attributes.hasKey("key3"));
        Assert.assertEquals(1, attributes.size());

        Assert.assertNull(attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testIterateAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> expectedEntries = Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"));

        for (Map.Entry<String, String> entry : attributes.asList()) {
            Assert.assertEquals(expectedEntries.get(0).getKey(), entry.getKey());
            Assert.assertEquals(expectedEntries.get(0).getValue(), entry.getValue());
            expectedEntries.remove(0);
        }

        Assert.assertEquals(0, expectedEntries.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expectedDataMap = new HashMap<>();
        expectedDataMap.put("key1", "value1");
        expectedDataMap.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(expectedDataMap, dataset);
    }
}