package org.jsoup.nodes;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributesTest {

    @org.junit.Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Test"));
        Assert.assertFalse(attributes.hasKey("test1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("TEST1"));
    }

    @org.junit.Test
    public void testBasicAttributeOperations() {
        Attributes attributes = new Attributes();

        attributes.put("test", "Test");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.hasKey("test1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("TEST1"));
        Assert.assertEquals("Test", attributes.get("test"));
        Assert.assertEquals("Test", attributes.getIgnoreCase("test"));
        Assert.assertEquals(EmptyString, attributes.get("test1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("TEST1"));

        attributes.put("test1", "Test1");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertTrue(attributes.hasKey("test1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("TEST1"));
        Assert.assertEquals("Test", attributes.get("test"));
        Assert.assertEquals("Test", attributes.getIgnoreCase("test"));
        Assert.assertEquals("Test1", attributes.get("test1"));
        Assert.assertEquals("Test1", attributes.getIgnoreCase("TEST1"));

        attributes.remove("test");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
        Assert.assertTrue(attributes.hasKey("test1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("TEST1"));
        Assert.assertEquals("Test1", attributes.get("test1"));
        Assert.assertEquals("Test1", attributes.getIgnoreCase("TEST1"));

        attributes.remove("test1");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.hasKey("test1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("TEST1"));
    }

    @org.junit.Test
    public void testBooleanAttributeOperations() {
        Attributes attributes = new Attributes();

        attributes.put("test", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.get("test").equals(EmptyString));
        Assert.assertEquals(EmptyString, attributes.get("test1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("TEST1"));

        attributes.remove("test");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
    }

    @org.junit.Test
    public void testAddAllAttributeOperations() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "Test1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "Test2");

        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("test1"));
        Assert.assertTrue(attributes1.hasKeyIgnoreCase("test1"));
        Assert.assertTrue(attributes1.hasKey("test2"));
        Assert.assertTrue(attributes1.hasKeyIgnoreCase("test2"));
        Assert.assertEquals("Test1", attributes1.get("test1"));
        Assert.assertEquals("Test1", attributes1.getIgnoreCase("test1"));
        Assert.assertEquals("Test2", attributes1.get("test2"));
        Assert.assertEquals("Test2", attributes1.getIgnoreCase("test2"));
    }

    @org.junit.Test
    public void testLowerCaseAttributeNames() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "TestValue");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Test"));
        Assert.assertEquals("TestValue", attributes.get("test"));
        Assert.assertEquals("TestValue", attributes.getIgnoreCase("Test"));
    }

    @org.junit.Test
    public void testPutIgnoreCaseAttributeOperations() {
        Attributes attributes = new Attributes();

        attributes.putIgnoreCase("test", "Test");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertEquals("Test", attributes.get("test"));
        Assert.assertEquals("Test", attributes.getIgnoreCase("test"));
        Assert.assertEquals(EmptyString, attributes.get("test1"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("TEST1"));

        attributes.putIgnoreCase("TEST", "Test2");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertTrue(attributes.hasKey("TEST"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Test"));
        Assert.assertEquals("Test", attributes.get("test"));
        Assert.assertEquals("Test", attributes.getIgnoreCase("test"));
        Assert.assertEquals("Test2", attributes.get("TEST"));
        Assert.assertEquals("Test2", attributes.getIgnoreCase("Test"));

        attributes.removeIgnoreCase("TEST");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertEquals("Test", attributes.get("test"));
        Assert.assertEquals("Test", attributes.getIgnoreCase("test"));
    }

    @org.junit.Test
    public void testPutAttributeOperationsWithSameKey() {
        Attributes attributes = new Attributes();

        attributes.put("test", "Test1");
        attributes.put("test", "Test2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertEquals("Test2", attributes.get("test"));
        Assert.assertEquals("Test2", attributes.getIgnoreCase("test"));

        attributes.put("test", "Test3");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertEquals("Test3", attributes.get("test"));
        Assert.assertEquals("Test3", attributes.getIgnoreCase("test"));
    }

    @org.junit.Test
    public void testCloneAttributeOperations() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "test1_value");
        Attributes attributes2 = attributes1.clone();

        attributes2.put("test2", "test2_value");

        Assert.assertEquals(1, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("test1"));
        Assert.assertTrue(attributes1.hasKeyIgnoreCase("test1"));
        Assert.assertEquals("test1_value", attributes1.get("test1"));
        Assert.assertEquals("test1_value", attributes1.getIgnoreCase("test1"));

        Assert.assertEquals(2, attributes2.size());
        Assert.assertTrue(attributes2.hasKey("test1"));
        Assert.assertTrue(attributes2.hasKeyIgnoreCase("test1"));
        Assert.assertEquals("test1_value", attributes2.get("test1"));
        Assert.assertEquals("test1_value", attributes2.getIgnoreCase("test1"));
        Assert.assertTrue(attributes2.hasKey("test2"));
        Assert.assertTrue(attributes2.hasKeyIgnoreCase("test2"));
        Assert.assertEquals("test2_value", attributes2.get("test2"));
        Assert.assertEquals("test2_value", attributes2.getIgnoreCase("test2"));
    }

    @org.junit.Test
    public void testDatasetAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "test1_value");
        attributes.put("data-test2", "test2_value");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals(2, dataset.size());
        Assert.assertTrue(attributes.hasKey("data-test1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-test1"));
        Assert.assertEquals("test1_value", attributes.get("data-test1"));
        Assert.assertEquals("test1_value", attributes.getIgnoreCase("data-test1"));
        Assert.assertEquals("test1_value", attributes.get("data-test1").getValue());
        Assert.assertEquals("test1_value", attributes.getIgnoreCase("data-test1").getValue());
        Assert.assertTrue(attributes.hasKey("data-test2"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-test2"));
        Assert.assertEquals("test2_value", attributes.get("data-test2"));
        Assert.assertEquals("test2_value", attributes.getIgnoreCase("data-test2"));
        Assert.assertEquals("test2_value", attributes.get("data-test2").getValue());
        Assert.assertEquals("test2_value", attributes.getIgnoreCase("data-test2").getValue());
        Assert.assertEquals("test1_value", dataset.get("data-test1"));
        Assert.assertEquals("test1_value", dataset.get("test1_value"));
        Assert.assertEquals("test2_value", dataset.get("data-test2"));
        Assert.assertEquals("test2_value", dataset.get("test2_value"));
    }

    @org.junit.Test
    public void testIteratorAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "test1_value");
        attributes.put("test2", "test2_value");

        List<Map.Entry<String, String>> expectedList = new ArrayList<>();
        expectedList.add(new AbstractMap.SimpleEntry<>("test1", "test1_value"));
        expectedList.add(new AbstractMap.SimpleEntry<>("test2", "test2_value"));

        List<Map.Entry<String, String>> actualList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            actualList.add(entry);
        }

        Assert.assertEquals(expectedList, actualList);
    }
}