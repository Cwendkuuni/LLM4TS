package org.test.jsoup.nodes; // replace with the actual package name for your test class

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testBasicPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("Value", attributes.getIgnoreCase("Key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertEquals("booleanKey", attributes.get("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").equals("false"));
        Assert.assertEquals(EmptyString, attributes.get("booleanKey2"));
        attributes.put("booleanKey", false);
        Assert.assertEquals(EmptyString, attributes.get("booleanKey"));
        Assert.assertFalse(attributes.hasKey("booleanKey"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(1, attributes.size());
        attributes.remove("key2");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "Value1");
        attributes.putIgnoreCase("Key2", "Value2");
        Assert.assertEquals(2, attributes.size());
        attributes.removeIgnoreCase("Key1");
        Assert.assertEquals("Value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(1, attributes.size());
        attributes.removeIgnoreCase("Key2");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("otherKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals(2, attributes2.size());
        Assert.assertEquals(4, attributes3.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Iterator<Attribute> iterator = attributes.iterator();
        List<Attribute> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Assert.assertEquals(2, list.size());
        for (Attribute attribute : list) {
            Assert.assertNotNull(attribute.getKey());
            Assert.assertNotNull(attribute.getValue());
        }
    }

    @Test
    public void testList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        Assert.assertEquals(2, attributesList.size());
        for (Attribute attribute : attributesList) {
            Assert.assertNotNull(attribute.getKey());
            Assert.assertNotNull(attribute.getValue());
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String html = attributes.html();
        Assert.assertEquals("<key1>value1</key1>", html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Assert.assertEquals(attributes1, attributes2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY1", "Value1");
        attributes.normalize();
        Assert.assertEquals("key1", attributes.get("key1"));
    }

    
}