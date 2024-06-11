package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        String expectedValue = "value";
        String actualValue = attributes.get("key");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        String expectedValue = "Value";
        String actualValue = attributes.getIgnoreCase("key");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetMethodWithEmptyAttribute() {
        Attributes attributes = new Attributes();

        String actualValue = attributes.get("nonExistentKey");

        Assert.assertEquals(Attributes.EmptyString, actualValue);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");

        String expectedValue = "value";
        String actualValue = attributes.get("key");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPutMethodWithEmptyAttributeValue() {
        Attributes attributes = new Attributes();

        attributes.put("key", "");

        String expectedValue = "";
        String actualValue = attributes.get("key");

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPutMethodWithExistingAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");

        String expectedValue = "value2";
        String actualValue = attributes.get("key");

        Assert.assertEquals(expectedValue, actualValue);
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
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");

        String actualValue = attributes.get("key");

        Assert.assertEquals(Attributes.EmptyString, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveMethodWithNullKey() {
        Attributes attributes = new Attributes();

        attributes.remove(null);
    }

    @Test
    public void testPutAndRemoveMethods() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String key1ExpectedValue = "value1";
        String key1ActualValue = attributes.get("key1");

        Assert.assertEquals(key1ExpectedValue, key1ActualValue);

        String key2ExpectedValue = "value2";
        String key2ActualValue = attributes.get("key2");

        Assert.assertEquals(key2ExpectedValue, key2ActualValue);

        attributes.remove("key1");

        String key1ActualValueAfterRemoval = attributes.get("key1");

        Assert.assertEquals(Attributes.EmptyString, key1ActualValueAfterRemoval);

        String key2ActualValueAfterRemoval = attributes.get("key2");

        Assert.assertEquals(key2ExpectedValue, key2ActualValueAfterRemoval);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        boolean expectedResult = true;
        boolean actualResult = attributes.hasKey("key");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        boolean expectedResult = true;
        boolean actualResult = attributes.hasKeyIgnoreCase("key");

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHasKeyMethodWithEmptyAttribute() {
        Attributes attributes = new Attributes();

        boolean actualResult = attributes.hasKey("nonExistentKey");

        Assert.assertEquals(false, actualResult);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();

        int expectedSize = 0;
        int actualSize = attributes.size();

        Assert.assertEquals(expectedSize, actualSize);

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        expectedSize = 2;
        actualSize = attributes.size();

        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("key1", "value1");
        expectedAttributes.put("key2", "value2");

        expectedAttributes.addAll(attributes1);

        int expectedSize = 2;
        int actualSize = expectedAttributes.size();

        Assert.assertEquals(expectedSize, actualSize);

        attributes1.addAll(expectedAttributes);

        Attribute[] expectedAttributesArray = new Attribute[2];
        expectedAttributesArray[0] = new Attribute("key1", "value1", attributes1);
        expectedAttributesArray[1] = new Attribute("key2", "value2", attributes1);

        Attribute[] actualAttributesArray = attributes1.asList().toArray(new Attribute[0]);

        Assert.assertArrayEquals(expectedAttributesArray, actualAttributesArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllMethodWithNullAttributes() {
        Attributes attributes = new Attributes();

        attributes.addAll(null);
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> expectedList = Arrays.asList(
                new Attribute("key1", "value1", attributes),
                new Attribute("key2", "value2", attributes)
        );

        List<Attribute> actualList = attributes.asList();

        Assert.assertEquals(expectedList, actualList);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAsListMethodWithModifyList() {
        Attributes attributes = new Attributes();
        List<Attribute> attributeList = attributes.asList();

        attributeList.add(new Attribute("newKey", "newValue", attributes));
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> actualMap = attributes.dataset();

        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testDatasetMethodWithNoDataAttributes() {
        Attributes attributes = new Attributes();

        Map<String, String> actualMap = attributes.dataset();

        Assert.assertEquals(Collections.emptyMap(), actualMap);
    }
}