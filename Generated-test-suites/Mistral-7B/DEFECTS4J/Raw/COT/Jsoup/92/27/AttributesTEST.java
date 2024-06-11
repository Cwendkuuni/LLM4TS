package org.jsoup.nodes; // Update the package name if necessary

import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        String actual = attributes.get("key1");
        String expected = "value1";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");

        String actual = attributes.getIgnoreCase("key1");
        String expected = "Value1";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyMethod() {
        Attributes attributes = new Attributes();

        String actual = attributes.get("nonExistentKey");
        String expected = "";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethod() {
        Attributes attributes = new Attributes();

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String actualKey1 = attributes.get("key1");
        String expectedKey1 = "value1";
        Assert.assertEquals(expectedKey1, actualKey1);

        String actualKey2 = attributes.get("key2");
        String expectedKey2 = "value2";
        Assert.assertEquals(expectedKey2, actualKey2);
    }

    @Test
    public void testPutIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");

        String actualKey1 = attributes.getIgnoreCase("key1");
        String expectedKey1 = "Value1";
        Assert.assertEquals(expectedKey1, actualKey1);

        String actualKey2 = attributes.getIgnoreCase("key2");
        String expectedKey2 = "Value2";
        Assert.assertEquals(expectedKey2, actualKey2);
    }

    @Test
    public void testPutMethodWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");

        String actual = attributes.get("key1");
        String expected = "value2";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutBooleanMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        boolean actual = attributes.getBoolean("key1");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        attributes.put("key1", false);
        actual = attributes.getBoolean("key1");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPutMethodWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", true);
        attributes.put("key1", false);

        boolean actual = attributes.getBoolean("key1");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValueMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", null);
    }

    @Test
    public void testRemoveMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        int sizeBeforeRemove = attributes.size();
        attributes.remove("key1");
        int sizeAfterRemove = attributes.size();

        Assert.assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
    }

    @Test
    public void testRemoveIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");

        int sizeBeforeRemove = attributes.size();
        attributes.removeIgnoreCase("key1");
        int sizeAfterRemove = attributes.size();

        Assert.assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
    }

    @Test
    public void testRemoveNonExistentKeyMethod() {
        Attributes attributes = new Attributes();

        int sizeBeforeRemove = attributes.size();
        attributes.remove("nonExistentKey");
        int sizeAfterRemove = attributes.size();

        Assert.assertEquals(sizeBeforeRemove, sizeAfterRemove);
    }

    @Test
    public void testHasKeyMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        boolean actual = attributes.hasKey("key1");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        actual = attributes.hasKey("nonExistentKey");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHasKeyIgnoreCaseMethod() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");

        boolean actual = attributes.hasKeyIgnoreCase("key1");
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        actual = attributes.hasKeyIgnoreCase("NonExistentKey");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSizeMethod() {
        Attributes attributes = new Attributes();

        int sizeBeforeAdd = attributes.size();
        attributes.put("key1", "value1");
        int sizeAfterAdd = attributes.size();

        Assert.assertEquals(sizeAfterAdd, sizeBeforeAdd + 1);
    }

    @Test
    public void testAddAllMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(attributes1);
        mergedAttributes.addAll(attributes2);

        boolean hasKey1 = mergedAttributes.hasKey("key1");
        boolean hasKey2 = mergedAttributes.hasKey("key2");

        Assert.assertTrue(hasKey1);
        Assert.assertTrue(hasKey2);
    }

    @Test
    public void testIteratorMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Iterator<Attribute> iterator = attributes.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            String key = attribute.getKey();
            String value = attribute.getValue();

            Assert.assertEquals("key" + count, key);
            Assert.assertEquals("value" + count, value);
            count++;
        }
    }

    @Test
    public void testAsListMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> attributesList = attributes.asList();
        Attribute attribute1 = attributesList.get(0);
        Attribute attribute2 = attributesList.get(1);

        String key1 = attribute1.getKey();
        String value1 = attribute1.getValue();

        String key2 = attribute2.getKey();
        String value2 = attribute2.getValue();

        Assert.assertEquals("key1", key1);
        Assert.assertEquals("value1", value1);

        Assert.assertEquals("key2", key2);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void testDatasetMethod() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        String dataKey1 = dataset.get("data-key1");
        String dataKey2 = dataset.get("data-key2");

        Assert.assertEquals("value1", dataKey1);
        Assert.assertEquals("value2", dataKey2);
    }

    @Test
    public void testHtmlMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String html = attributes.html();

        String expected = " key1=\"value1\" key2=\"value2\"";
        Assert.assertEquals(expected, html);
    }

    @Test
    public void testEqualMethod() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value3");
        attributes3.put("key2", "value2");

        boolean equals1 = attributes1.equals(attributes2);
        boolean equals2 = attributes1.equals(attributes3);

        Assert.assertTrue(equals1);
        Assert.assertFalse(equals2);
    }

    @Test
    public void testCloneMethod() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        Attributes clonedAttributes2 = null;

        try {
            clonedAttributes2 = (Attributes) attributes.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        String key1 = clonedAttributes.get("key1");
        String key2 = clonedAttributes.get("key2");

        String key1Clone = clonedAttributes2.get("key1");
        String key2Clone = clonedAttributes2.get("key2");

        Assert.assertEquals(key1, key1Clone);
        Assert.assertEquals(key2, key2Clone);

        int sizeClone = clonedAttributes.size();
        int sizeClone2 = clonedAttributes2.size();

        Assert.assertEquals(sizeClone, sizeClone2);

        boolean equalsClone = clonedAttributes.equals(attributes);
        boolean equalsClone2 = clonedAttributes2.equals(attributes);

        Assert.assertTrue(equalsClone);
        Assert.assertTrue(equalsClone2);
    }
}