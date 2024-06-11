package org.jsoup.nodes;

import org.jsoup.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @org.junit.Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(attributes.get("test"));
        Assert.assertFalse(attributes.hasKey("test"));
    }

    @org.junit.Test
    public void testSetGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("test", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("test"));
        Assert.assertEquals("value", attributes.getIgnoreCase("Test"));
        Assert.assertEquals("value", attributes.getIgnoreCase("tEsT"));
        Assert.assertTrue(attributes.hasKey("test"));
    }

    @org.junit.Test
    public void testSetGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertEquals("value", attributes.get("test"));
        Assert.assertNull(attributes.get("other"));
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("other"));
    }

    @org.junit.Test
    public void testSetBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        Assert.assertNull(attributes.get("test"));
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("other"));
    }

    @org.junit.Test
    public void testSetBooleanAttributeRemove() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        attributes.put("test", false);
        Assert.assertFalse(attributes.hasKey("test"));
    }

    @org.junit.Test
    public void testPutAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("test2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.putAll(attributes1);
        attributes3.putAll(attributes2);

        Assert.assertEquals("value1", attributes3.get("test1"));
        Assert.assertEquals("value2", attributes3.get("test2"));
    }

    @org.junit.Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");

        List<Map.Entry<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
            list.add(entry);
        }

        Assert.assertEquals(2, list.size());
        Assert.assertEquals("test1", list.get(0).getKey());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("test2", list.get(1).getKey());
        Assert.assertEquals("value2", list.get(1).getValue());
    }

    @org.junit.Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-test1", "value1");
        attributes.put("data-test2", "value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-test1"));
        Assert.assertEquals("value2", dataset.get("data-test2"));
    }

    @org.junit.Test
    public void testHashCodeAndEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("test", "value1");

        Attributes attributes2 = new Attributes();
        attributes2.put("test", "value2");

        Assert.assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertFalse(attributes1.equals(attributes2));

        Attributes attributes3 = new Attributes();
        attributes3.put("test", "value1");
        Assert.assertEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertTrue(attributes1.equals(attributes3));
    }

    @org.junit.Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value1");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals("value1", clonedAttributes.get("test"));
        Assert.assertFalse(attributes.equals(clonedAttributes));
    }
}