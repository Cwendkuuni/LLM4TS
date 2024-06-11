package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key";
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newkey");
        String expected = "newkey";
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "value";
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newvalue");
        String expected = "newvalue";
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        String actual = attribute.html();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        String actual = attribute.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int expected = 36168988;
        int actual = attribute.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}