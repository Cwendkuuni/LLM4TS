package org.jsoup.nodes; // Make sure to import all the necessary classes from the Attributes class

import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String result = attributes.get("test");
        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String result = attributes.getIgnoreCase("test");
        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testGetNullMethod() {
        Attributes attributes = new Attributes();

        String result = attributes.get("nonExistentKey");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Assert.assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);

        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("test2"));
    }

    @Test
    public void testPutAttributeMethod() {
        Attributes attributes = new Attributes();
        Attribute testAttribute = new Attribute("test", "testValue");
        attributes.put(testAttribute);

        Assert.assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        attributes.remove("test1");

        Assert.assertFalse(attributes.hasKey("test1"));
        Assert.assertTrue(attributes.hasKey("test2"));
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Test", "TestValue");

        attributes.removeIgnoreCase("test"); // Should not remove since keys are case sensitive
        attributes.removeIgnoreCase("Test");

        Assert.assertFalse(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("Test"));
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("Test"));
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "testValue2");
        attributes2.put("test3", "testValue3");

        attributes1.addAll(attributes2);

        Assert.assertEquals(3, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("test1"));
        Assert.assertTrue(attributes1.hasKey("test2"));
        Assert.assertTrue(attributes1.hasKey("test3"));
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        List<Attribute> attributesList = attributes.asList();

        for (Attribute attribute : attributesList) {
            Assert.assertEquals(attribute.getKey(), "test1"
                    || "test2");
        }
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        List<Attribute> attributesList = attributes.asList();

        Assert.assertEquals(2, attributesList.size());
        Assert.assertTrue(attributesList.get(0).getKey().equals("test1"));
        Assert.assertTrue(attributesList.get(1).getKey().equals("test2"));
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-test", "testValue");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals("testValue", dataset.get("data-test"));
        Assert.assertFalse(dataset.containsKey("test"));
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String html = attributes.html();

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" test=\"testValue\"");

        Assert.assertEquals(expectedHtml.toString(), html);
    }

    @Test
    public void testToStringMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");

        String toString = attributes.toString();

        StringBuilder expectedToString = new StringBuilder();
        expectedToString.append("Attributes{attributes=");
        expectedToString.append("{test=testValue}}");

        Assert.assertEquals(expectedToString.toString(), toString);
    }

    @Test
    public void testEqualsMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");
        attributes1.put("test2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "testValue1");
        attributes2.put("test2", "testValue2");

        Attributes attributes3 = new Attributes();
        attributes3.put("test1", "testValue1");
        attributes3.put("test3", "testValue3");

        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCodeMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "testValue1");
        attributes1.put("test2", "testValue2");

        Attributes attributes2 = new Attributes();
        attributes2.put("test1", "testValue1");
        attributes2.put("test2", "testValue2");

        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), new Attributes().hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "testValue1");
        attributes.put("test2", "testValue2");

        Attributes clonedAttributes = attributes.clone();

        Assert.assertEquals(attributes, clonedAttributes);
        Assert.assertNotSame(attributes, clonedAttributes);
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertTrue(attributes.hasKey("test1"));
        Assert.assertTrue(clonedAttributes.hasKey("test1"));
        Assert.assertTrue(attributes.hasKey("test2"));
        Assert.assertTrue(clonedAttributes.hasKey("test2"));
    }

    
}