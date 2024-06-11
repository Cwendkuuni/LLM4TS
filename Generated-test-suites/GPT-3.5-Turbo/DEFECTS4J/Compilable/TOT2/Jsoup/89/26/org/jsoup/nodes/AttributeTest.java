package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1", attribute.getKey());
        
        attribute = new Attribute("key2", "value2");
        assertEquals("key2", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("value1", attribute.getValue());
        
        attribute = new Attribute("key2", "");
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("value1", attribute.setValue("newValue"));
        
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }

    // Add more tests for other methods as needed

}