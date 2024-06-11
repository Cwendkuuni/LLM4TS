package org.jsoup.nodes; // assuming the test class is in the same package

import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

    

    @Test
    public void testGetEmptyAttributes() {
        Attributes attributes = new Attributes();
        String result = attributes.get("testKey");
        Assert.assertEquals("", result);
    }

    @Test
    public void testGetSetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        String result = attributes.get("testKey");
        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testGetIgnoreCaseAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        String result = attributes.getIgnoreCase("TestKey");
        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testAddAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes mergedAttributes = new Attributes();
        mergedAttributes.addAll(attributes1);
        mergedAttributes.addAll(attributes2);

        Assert.assertEquals(2, mergedAttributes.size());
        Assert.assertTrue(mergedAttributes.hasKey("key1"));
        Assert.assertEquals("value1", mergedAttributes.get("key1"));
        Assert.assertTrue(mergedAttributes.hasKey("key2"));
        Assert.assertEquals("value2", mergedAttributes.get("key2"));
    }

    @Test
    public void testGetEmptyStringForNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", null);
        String result = attributes.get("testKey");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", true);
        Assert.assertTrue(attributes.hasKey("testKey"));
        Assert.assertEquals("", attributes.get("testKey"));

        attributes.remove("testKey");
    }

    @Test
    public void testPutAndRemoveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("testKey1"));
        Assert.assertEquals("testValue1", attributes.get("testKey1"));
        Assert.assertTrue(attributes.hasKey("testKey2"));
        Assert.assertEquals("testValue2", attributes.get("testKey2"));

        attributes.remove("testKey1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("testKey1"));
        Assert.assertEquals("testValue2", attributes.get("testKey2"));

        attributes.remove("testKey2");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("testKey1"));
        Assert.assertFalse(attributes.hasKey("testKey2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        attributes.put("testKey", "testValue2");
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> attributesList = new ArrayList<>();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            attributesList.add(attribute);
        }

        Assert.assertEquals(2, attributesList.size());
        Assert.assertEquals("testKey1", attributesList.get(0).getKey());
        Assert.assertEquals("testValue1", attributesList.get(0).getValue());
        Assert.assertEquals("testKey2", attributesList.get(1).getKey());
        Assert.assertEquals("testValue2", attributesList.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        List<Attribute> attributesList = attributes.asList();
        Assert.assertEquals(2, attributesList.size());
        Assert.assertEquals("testKey1", attributesList.get(0).getKey());
        Assert.assertEquals("testValue1", attributesList.get(0).getValue());
        Assert.assertEquals("testKey2", attributes.get(1).getKey());
        Assert.assertEquals("testValue2", attributesList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("dataKey1", dataset.get("dataKey1"));
        Assert.assertEquals("dataValue1", dataset.get("dataKey1"));
        Assert.assertEquals("dataKey2", dataset.get("dataKey2"));
        Assert.assertEquals("dataValue2", dataset.get("dataKey2"));
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        String result = attributes.toString();
        String expected = "Attributes{size=2, keys=[testKey1, testKey2], vals=[testValue1, testValue2], parent=null}";
        Assert.assertEquals(expected, result);
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
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        int hashCode1 = attributes.hashCode();
        attributes.normalize();
        int hashCode2 = attributes.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Attributes clonedAttributes = (Attributes) attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertEquals(attributes.hashCode(), clonedAttributes.hashCode());
        Assert.assertNotSame(attributes, clonedAttributes);

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedKey = clonedAttributes.get(key);
            String clonedValue = clonedAttributes.get(key);

            Assert.assertEquals(key, clonedKey);
            Assert.assertEquals(value, clonedValue);
        }
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("TEST_KEY", "testValue");

        attributes.normalize();
        Assert.assertEquals("testKey", attributes.get("testKey"));
        Assert.assertEquals("testValue", attributes.get("testKey"));
    }

    
}