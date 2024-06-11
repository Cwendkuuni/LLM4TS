package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("Key2"));
    }

    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key1", true);
        attributes.put("key2", true);
        attributes.put("key3", false);

        Assert.assertEquals("true", attributes.get("key1"));
        Assert.assertEquals("true", attributes.get("key2"));
        Assert.assertEquals("", attributes.get("key3"));
    }

    @Test
    public void testPutAttribute() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");

        attributes.put(attribute1);
        attributes.put(attribute2);

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");

        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.removeIgnoreCase("KEY1");

        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key2"));
        Assert.assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        Assert.assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.asList().size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Assert.assertEquals("value1", attributes.dataset().get("key1"));
        Assert.assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Assert.assertTrue(attributes.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Assert.assertEquals(attributes2.hashCode(), attributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        Assert.assertEquals(attributes.size(), clone.size());
        Assert.assertEquals(attributes.get("key1"), clone.get("key1"));
        Assert.assertEquals(attributes.get("key2"), clone.get("key2"));
    }
}