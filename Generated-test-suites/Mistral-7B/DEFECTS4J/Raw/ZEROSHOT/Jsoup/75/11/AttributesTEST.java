package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.Nodes.Attributes.AttributesBuilder.*;

public class AttributesTest {

    @Test
    public void testAttributesConstructor() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(attributes.get("test"));
    }

    @Test
    public void testAttributesPutGet() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testAttributesPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("test", "value");
        Assert.assertEquals("value", attributes.get("test"));
    }

    @Test
    public void testAttributesPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("test", true);
        Assert.assertNull(attributes.get("test"));
        Assert.assertTrue(attributes.hasKey("test"));
    }

    @Test
    public void testAttributesHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertTrue(attributes.hasKey("test"));
        Assert.assertFalse(attributes.hasKey("other"));
    }

    @Test
    public void testAttributesHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("other"));
    }

    @Test
    public void testAttributesSize() {
        Attributes attributes = new Attributes();
        attributes.put("test1", "value1");
        attributes.put("test2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAttributesAddAll() {
        Attributes attributes1 = new Attributes().put("test1", "value1");
        Attributes attributes2 = new Attributes().put("test2", "value2");
        Attributes expectedAttributes = new Attributes().put("test1", "value1").put("test2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(expectedAttributes.size(), attributes1.size());
        Assert.assertEquals(expectedAttributes.get("test1"), attributes1.get("test1"));
        Assert.assertEquals(expectedAttributes.get("test2"), attributes1.get("test2"));
    }

    @Test
    public void testAttributesClone() {
        Attributes attributes = new Attributes().put("test1", "value1").put("test2", "value2");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.size(), clonedAttributes.size());
        Assert.assertEquals(attributes.get("test1"), clonedAttributes.get("test1"));
        Assert.assertEquals(attributes.get("test2"), clonedAttributes.get("test2"));
    }

    @Test
    public void testAttributesToString() {
        Attributes attributes = new Attributes().put("test1", "value1").put("test2", "value2");
        Assert.assertEquals("test1=\"value1\" test2=\"value2\"", attributes.toString());
    }

    @Test
    public void testAttributesEquals() {
        Attributes attributes1 = new Attributes().put("test1", "value1").put("test2", "value2");
        Attributes attributes2 = new Attributes().put("test1", "value1").put("test2", "value2");
        Attributes attributes3 = new Attributes().put("test1", "value1").put("test3", "value3");
        Attributes attributes4 = new Attributes().put("test2", "value2").put("test1", "value1");
        Assert.assertTrue(attributes1.equals(attributes1));
        Assert.assertTrue(attributes1.equals(attributes2));
        Assert.assertFalse(attributes1.equals(attributes3));
        Assert.assertFalse(attributes1.equals(attributes4));
    }

    @Test
    public void testAttributesHashCode() {
        Attributes attributes1 = new Attributes().put("test1", "value1").put("test2", "value2");
        Attributes attributes2 = new Attributes().put("test1", "value1").put("test2", "value2");
        Attributes attributes3 = new Attributes().put("test1", "value1").put("test3", "value3");
        Attributes attributes4 = new Attributes().put("test2", "value2").put("test1", "value1");
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
        Assert.assertNotEquals(attributes1.hashCode(), attributes4.hashCode());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes().put("data-test1", "value1").put("data-test2", "value2");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value1", dataset.get("data-test1"));
        Assert.assertEquals("value2", dataset.get("data-test2"));
    }
}