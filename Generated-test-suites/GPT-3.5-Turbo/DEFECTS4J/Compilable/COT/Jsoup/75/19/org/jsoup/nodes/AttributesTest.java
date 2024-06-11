package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet() {
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testGetIgnoreCase() {
        attributes.put("Name", "John");
        Assert.assertEquals("John", attributes.getIgnoreCase("name"));
    }

    @Test
    public void testPut() {
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testPutBooleanTrue() {
        attributes.put("required", true);
        Assert.assertTrue(attributes.hasKeyIgnoreCase("required"));
    }

    @Test
    public void testPutBooleanFalse() {
        attributes.put("required", false);
        Assert.assertFalse(attributes.hasKeyIgnoreCase("required"));
    }

    @Test
    public void testRemove() {
        attributes.put("name", "John");
        attributes.remove("name");
        Assert.assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Name", "John");
        attributes.removeIgnoreCase("name");
        Assert.assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testHasKey() {
        attributes.put("name", "John");
        Assert.assertTrue(attributes.hasKey("name"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Name", "John");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("name"));
    }

    @Test
    public void testSize() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("name", "John");
        incoming.put("age", "25");
        attributes.addAll(incoming);
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testIterator() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Iterator<Attribute> iterator = attributes.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        Assert.assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        List<Attribute> attributeList = attributes.asList();
        Assert.assertEquals(2, attributeList.size());
    }

    @Test
    public void testDataset() {
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value", dataset.get("key"));
    }

    @Test
    public void testHtml() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        String html = attributes.html();
        Assert.assertEquals(" name=\"John\" age=\"25\"", html);
    }

    @Test
    public void testEquals() {
        attributes.put("name", "John");
        attributes.put("age", "25");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("name", "John");
        otherAttributes.put("age", "25");

        Assert.assertEquals(attributes, otherAttributes);
    }

    @Test
    public void testHashCode() {
        attributes.put("name", "John");
        attributes.put("age", "25");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("name", "John");
        otherAttributes.put("age", "25");

        Assert.assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone() {
        attributes.put("name", "John");
        attributes.put("age", "25");

        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes, clonedAttributes);
    }

    @Test
    public void testNormalize() {
        attributes.put("name", "John");
        attributes.put("Name", "John");
        attributes.normalize();
        Assert.assertFalse(attributes.hasKey("Name"));
        Assert.assertTrue(attributes.hasKey("name"));
    }
}