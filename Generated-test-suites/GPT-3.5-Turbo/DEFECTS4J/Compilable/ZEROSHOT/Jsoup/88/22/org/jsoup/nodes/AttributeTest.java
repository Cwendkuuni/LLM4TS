package org.jsoup.nodes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Assert.assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute other = new Attribute("key", "value");
        Assert.assertEquals(attribute, other);
    }

    @Test
    public void testHashCode() {
        Attribute other = new Attribute("key", "value");
        Assert.assertEquals(attribute.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        Assert.assertEquals(attribute, clone);
    }
}