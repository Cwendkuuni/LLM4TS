package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testConstructorAndInitialState() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertArrayEquals(Attributes.EMPTY, attributes.keys);
        Assert.assertArrayEquals(Attributes.EMPTY, attributes.vals);
    }

    @Test
    public void testCapacity() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(5);
        Assert.assertArrayEquals(new String[5], attributes.keys);
        Assert.assertArrayEquals(new String[5], attributes.vals);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCapacity() {
        Attributes attributes = new Attributes();
        attributes.checkCapacity(-1);
    }

    @Test
    public void testGetAndSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        Assert.assertEquals("TestValue", attributes.getIgnoreCase("testkey"));
    }

    @Test
    public void testAdd() {
        Attributes attributes = new Attributes();
        attributes.add("testKey", "testValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("testValue", attributes.get("testKey"));

        attributes.put("testKey", "newTestValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("newTestValue", attributes.get("testKey"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("TestValue", attributes.get("TestKey"));

        attributes.putIgnoreCase("testkey", "newTestValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("newTestValue", attributes.get("TestKey"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);
        Assert.assertFalse(attributes.get("testKey").isEmpty());

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey", false);
        Assert.assertEquals(EmptyString, attributes2.get("testKey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("testKey", "testValue");
        attributes.put(attribute);
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("testValue", attributes.get("testKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Assert.assertEquals(1, attributes.size());
        attributes.remove("testKey");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        Assert.assertEquals(1, attributes.size());
        attributes.removeIgnoreCase("testkey");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        Assert.assertTrue(attributes.hasKey("testKey"));
        Assert.assertFalse(attributes.hasKey("otherKey"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("TestKey", "TestValue");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("testkey"));
        Assert.assertFalse(attributes.hasKey("otherKey"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("OtherKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        Attributes attributes2 = new Attributes();
        attributes2.put("testKey2", "testValue2");

        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals("testValue1", attributes1.get("testKey1"));
        Assert.assertEquals("testValue2", attributes1.get("testKey2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Map.Entry<String, String>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("testKey1", "testValue1"),
                new AbstractMap.SimpleEntry<>("testKey2", "testValue2")
        );
        List<Map.Entry<String, String>> actualList = new ArrayList<>();

        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("testKey1", "testValue1"),
                new Attribute("testKey2", "testValue2")
        );
        List<Attribute> actualList = attributes.asList();

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-testKey1", "data-testValue1");
        attributes.put("data-testKey2", "data-testValue2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("testKey1", "testValue1");
        expectedMap.put("testKey2", "testValue2");

        Map<String, String> actualMap = attributes.dataset();

        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" testKey=\"testValue\"");
        StringBuilder actualHtml = new StringBuilder();

        try {
            attributes.html(actualHtml, null);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        Assert.assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append(" testKey=\"testValue\"");

        Assert.assertEquals(expectedToString.toString(), attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("testKey1", "testValue1");
        attributes1.put("testKey2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("testKey1", "testValue1");
        attributes2.put("testKey2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.put("testKey1", "testValue1");
        attributes3.put("testKey3", "testValue3");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(attributes, clonedAttributes);
    }
}