package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        String value = attributes.get("name");
        
        Assert.assertEquals("John", value);
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        String value = attributes.getIgnoreCase("NAME");
        
        Assert.assertEquals("John", value);
    }
    
    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        
        attributes.put("name", "John");
        
        String value = attributes.get("name");
        
        Assert.assertEquals("John", value);
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("isVisible", true);

        boolean hasVisible = attributes.hasKey("isVisible");
        boolean isVisible = Boolean.parseBoolean(attributes.get("isVisible"));

        Assert.assertTrue(hasVisible);
        Assert.assertTrue(isVisible);
    }
    
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        attributes.remove("name");
        
        boolean hasName = attributes.hasKey("name");
        
        Assert.assertFalse(hasName);
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        attributes.removeIgnoreCase("NAME");
        
        boolean hasName = attributes.hasKey("name");
        
        Assert.assertFalse(hasName);
    }
    
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        boolean hasKey = attributes.hasKey("name");
        
        Assert.assertTrue(hasKey);
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        boolean hasKey = attributes.hasKeyIgnoreCase("NAME");
        
        Assert.assertTrue(hasKey);
    }
    
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        int size = attributes.size();
        
        Assert.assertEquals(1, size);
    }
    
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        Attributes copy = attributes.clone();
        
        Assert.assertEquals(attributes, copy);
    }
    
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-name", "John");
        
        Map<String, String> dataset = attributes.dataset();
        
        Assert.assertTrue(dataset.containsKey("name"));
        Assert.assertEquals("John", dataset.get("name"));
    }
    
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        List<Attribute> attributeList = attributes.asList();
        
        Assert.assertEquals(1, attributeList.size());
        Assert.assertEquals("name", attributeList.get(0).getKey());
        Assert.assertEquals("John", attributeList.get(0).getValue());
    }
    
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        
        String html = attributes.html();
        
        Assert.assertEquals(" name=\"John\"", html);
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
        
        int hashCode = attributes.hashCode();
        
        Assert.assertNotEquals(0, hashCode);
    }
}