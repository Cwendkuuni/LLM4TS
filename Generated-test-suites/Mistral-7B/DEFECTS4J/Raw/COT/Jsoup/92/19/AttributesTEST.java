package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.jsoup.nodes.Attributes.copyOf;
import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.Empty;
import static org.jsoup.nodes.Attributes.EMPTY_STRING;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;
import static org.jsoup.nodes.Attributes.NotFound;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testInitialState() {
        Assert.assertArrayEquals(Empty, attributes.keys);
        Assert.assertArrayEquals(Empty, attributes.vals);
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testAddGetMethod() {
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        attributes.put("key", null);
    }

    @Test
    public void testPutMethodIgnoreCase() {
        attributes.put("KEY", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutBooleanAttribute() {
        attributes.put("booleanKey", false);
        Assert.assertFalse(attributes.hasKey("booleanKey"));
        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());
    }

    @Test
    public void testPutAttributeWithExistingKey() {
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testRemoveMethod() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRemoveOutOfBounds() {
        attributes.remove(Attributes.NotFound);
    }

    @Test
    public void testHasKeyMethod() {
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasKeyNullKey() {
        attributes.hasKey(null);
    }

    @Test
    public void testSizeMethod() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key3", "value3");
        otherAttributes.put("key4", "value4");

        attributes.addAll(otherAttributes);
        Assert.assertEquals(4, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(attributes.hasKey("key3"));
        Assert.assertTrue(attributes.hasKey("key4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllNullAttributes() {
        Attributes otherAttributes = null;
        attributes.addAll(otherAttributes);
    }

    @Test
    public void testIteratorMethod() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        Assert.assertTrue(iterator.hasNext());
        Attribute firstAttribute = iterator.next();
        Assert.assertEquals("key1", firstAttribute.getKey());
        Assert.assertEquals("value1", firstAttribute.getValue());

        Assert.assertTrue(iterator.hasNext());
        Attribute secondAttribute = iterator.next();
        Assert.assertEquals("key2", secondAttribute.getKey());
        Assert.assertEquals("value2", secondAttribute.getValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsListMethod() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.get(0).getKey().equals("key1"));
        Assert.assertTrue(list.get(0).getValue().equals("value1"));
        Assert.assertTrue(list.get(1).getKey().equals("key2"));
        Assert.assertTrue(list.get(1).getValue().equals("value2"));
    }

    @Test
    public void testDatasetMethod() {
        attributes.put("key1", "value1");
        attributes.put("data:key2", "value2");
        attributes.put("data:key3", "value3");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(3, dataset.size());
        Assert.assertTrue(dataset.containsKey("key2"));
        Assert.assertTrue(dataset.containsKey("key3"));
        Assert.assertFalse(dataset.containsKey("key1"));
    }

    @Test
    public void testHtmlMethod() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        String attributesHtml = attributes.html();
        Assert.assertEquals(expectedHtml.toString(), attributesHtml);
    }

    @Test
    public void testCloneMethod() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");

        Attributes clonedAttributes = originalAttributes.clone();
        Assert.assertEquals(originalAttributes.size(), clonedAttributes.size());
        Assert.assertTrue(clonedAttributes.hasKey("key1"));
        Assert.assertTrue(clonedAttributes.hasKey("key2"));
        Assert.assertNotSame(originalAttributes, clonedAttributes);
    }

    @Test
    public void testNormalizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "value1");
        attributes.put("KEY2", "value2");
        attributes.normalize();
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testCheckCapacityMethod() {
        Attributes attributes = new Attributes();
        for (int i = 0; i < 10; i++) {
            attributes.put("key" + i, "value" + i);
        }
        Assert.assertEquals(10, attributes.size());
        attributes.checkCapacity(15);
        Assert.assertEquals(15, attributes.keys.length);
        Assert.assertEquals(15, attributes.vals.length);
    }

    @Test
    public void testCopyOfMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String[] expectedKeys = {"key1", "key2"};
        String[] expectedVals = {"value1", "value2"};

        String[] keys = copyOf(attributes.keys, attributes.size());
        String[] vals = copyOf(attributes.vals, attributes.size());

        Assert.assertArrayEquals(expectedKeys, keys);
        Assert.assertArrayEquals(expectedVals, vals);
    }
}