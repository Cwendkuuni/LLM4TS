package org.jsoup.nodes;
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
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Assert.assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "value&amp;");
        Assert.assertEquals("value&", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertFalse(attribute.isDataAttribute());
        Assert.assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(attribute.isBooleanAttribute());
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        Attribute differentKeyAttribute = new Attribute("differentKey", "value");
        Attribute differentValueAttribute = new Attribute("key", "differentValue");

        Assert.assertTrue(attribute.equals(equalAttribute));
        Assert.assertFalse(attribute.equals(differentKeyAttribute));
        Assert.assertFalse(attribute.equals(differentValueAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        Assert.assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), cloneAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }
}