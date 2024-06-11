package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    private Attributes parent;

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertEquals("key1", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertEquals("key1=\"value1\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertEquals("key1=\"value1\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key1", "value1");
        Assert.assertEquals("key1", attribute.getKey());
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value1", parent);
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "", parent);
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1", parent);
        Attribute attribute2 = new Attribute("key1", "value1", parent);
        Assert.assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertEquals(-570112043, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key1", "value1", parent);
        Assert.assertNotSame(attribute, attribute.clone());
    }
}