package org.jsoup.nodes;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        String expected = "key";
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetKey() {
        String expected = "newKey";
        attribute.setKey(expected);
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValue() {
        String expected = "value";
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetValue() {
        String expected = "newValue";
        attribute.setValue(expected);
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testHtml() {
        String expected = "key=\"value\"";
        String actual = attribute.html();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "key=\"value\"";
        String actual = attribute.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Attribute other = new Attribute("key", "value");
        assertTrue(attribute.equals(other));
    }

    @Test
    public void testHashCode() {
        Attribute other = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }
}