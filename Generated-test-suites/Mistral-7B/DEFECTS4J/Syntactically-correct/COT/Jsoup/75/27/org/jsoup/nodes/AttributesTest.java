package org.junit.tests; // replace with your test package name if needed

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {
    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String retrievedValue = attributes.get("key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        Assert.assertNull(attributes.get(null));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String retrievedValue = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String retrievedValue = attributes.get("key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "value");
        String retrievedValue = attributes.get("Key");
        Assert.assertEquals("value", retrievedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testPutIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "value");
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.get("key").equals("true"));
        Assert.assertFalse(attributes.hasKey("key2"));
        attributes.put("key2", false);
        Assert.assertFalse(attributes.get("key"));
        Assert.assertFalse(attributes.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutBooleanAttributeWithEmptyString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("Key1");
        Assert.assertFalse(attributes.hasKey("Key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase(null);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllWithNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.addAll(null);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> entries = attributes.asList();
        for (Map.Entry<String, String> entry : entries) {
            Assert.assertEquals(entry.getKey(), attributes.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), attributes.get(entry.getKey()));
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        List<Map.Entry<String, String>> entries = new ArrayList<>(dataset.entrySet());
        for (Map.Entry<String, String> entry : entries) {
            Assert.assertEquals(entry.getKey(), attributes.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), attributes.get(entry.getKey()));
        }
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key=\"value\"");
        Assert.assertEquals(expectedHtml.toString(), attributes.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlWithException() throws IOException {
        Attributes attributes = new Attributes();
        StringBuilder accum = new StringBuilder();
        attributes.html(accum, null);
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");
        Attributes clonedAttributes = originalAttributes.clone();
        Assert.assertEquals(originalAttributes.size(), clonedAttributes.size());
        Assert.assertEquals(originalAttributes.get("key1"), clonedAttributes.get("key1"));
        Assert.assertEquals(originalAttributes.get("key2"), clonedAttributes.get("key2"));
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes1, attributes1);
        Assert.assertEquals(attributes1, attributes2);
        Assert.assertNotEquals(attributes1, attributes3);
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.normalize();
        Assert.assertEquals("key", attributes.get("key"));
    }

    
}