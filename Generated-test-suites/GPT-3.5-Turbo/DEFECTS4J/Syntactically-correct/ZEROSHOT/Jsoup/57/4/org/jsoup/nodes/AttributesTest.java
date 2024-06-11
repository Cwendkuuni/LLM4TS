package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.getIgnoreCase("NAME"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("flag", true);
        Assert.assertEquals("true", attributes.get("flag"));
        attributes.put("flag", false);
        Assert.assertEquals("", attributes.get("flag"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("name", "John");
        attributes.put(attribute);
        Assert.assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.remove("name");
        Assert.assertEquals("", attributes.get("name"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.removeIgnoreCase("NAME");
        Assert.assertEquals("", attributes.get("name"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertTrue(attributes.hasKey("name"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("NAME"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");

        Attributes attributes2 = new Attributes();
        attributes2.addAll(attributes1);
        Assert.assertEquals("John", attributes2.get("name"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        List<Attribute> attributeList = attributes.asList();
        Assert.assertEquals("John", attributeList.get(0).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-name", "John");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("John", dataset.get("name"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals(" name=\"John\"", attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");

        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");

        Assert.assertEquals(attributes1, attributes2);
    }

    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");

        int hashcode = attributes.hashCode();
        Assert.assertNotEquals(0, hashcode);
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");

        Attributes attributes2 = attributes1.clone();
        Assert.assertEquals(attributes1, attributes2);
    }

    // Additional test cases can be added to cover all methods in the class

}