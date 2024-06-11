package org.jsoup.nodes;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "value");
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute_BooleanSyntax() {
        Attribute attribute = new Attribute("checked", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttribute_BooleanSyntaxWithValue() {
        Attribute attribute = new Attribute("checked", "checked");
        Assert.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttribute_HtmlSyntax() {
        Attribute attribute = new Attribute("checked", "");
        Assert.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttribute_HtmlSyntaxWithValue() {
        Attribute attribute = new Attribute("checked", "checked");
        Assert.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checkbox", "");
        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals_SameObject() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertTrue(attribute.equals(attribute));
    }

    @Test
    public void testEquals_NullObject() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertFalse(attribute.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertFalse(attribute.equals("name"));
    }

    @Test
    public void testEquals_DifferentKey() {
        Attribute attribute1 = new Attribute("name1", "value");
        Attribute attribute2 = new Attribute("name2", "value");
        Assert.assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testEquals_DifferentValue() {
        Attribute attribute1 = new Attribute("name", "value1");
        Attribute attribute2 = new Attribute("name", "value2");
        Assert.assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testEquals_SameKeyAndValue() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Assert.assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("name", "value");
        int expectedHashCode = 31 * "name".hashCode() + "value".hashCode();
        Assert.assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = attribute1.clone();
        Assert.assertEquals(attribute1.getKey(), attribute2.getKey());
        Assert.assertEquals(attribute1.getValue(), attribute2.getValue());
    }

}