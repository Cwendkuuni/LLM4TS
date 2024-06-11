package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void getKey_ShouldReturnCorrectKey() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name", attribute.getKey());
    }
    
    @Test
    public void setKey_ShouldUpdateKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }
    
    @Test
    public void getValue_ShouldReturnCorrectValue() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void setValue_ShouldUpdateValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }
    
    @Test
    public void html_ShouldReturnCorrectHtmlRepresentation() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.html());
    }
    
    @Test
    public void toString_ShouldReturnHtmlRepresentation() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.toString());
    }
    
    @Test
    public void createFromEncoded_ShouldReturnAttributeWithDecodedValue() {
        Attribute attribute = Attribute.createFromEncoded("name", "encoded&amp;value");
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals("encoded&value", attribute.getValue());
    }
    
    @Test
    public void isDataAttribute_ShouldReturnTrueForDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }
    
    @Test
    public void shouldCollapseAttribute_ShouldReturnTrueForBooleanAttributeWithEmptyValue() {
        Attribute attribute = new Attribute("checked", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void shouldCollapseAttribute_ShouldReturnTrueForBooleanAttributeWithSameValueAsKey() {
        Attribute attribute = new Attribute("checked", "checked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }
    
    @Test
    public void equals_ShouldReturnTrueForSameAttribute() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Assert.assertTrue(attribute1.equals(attribute2));
    }
    
    @Test
    public void equals_ShouldReturnFalseForDifferentAttribute() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "differentValue");
        Assert.assertFalse(attribute1.equals(attribute2));
    }
    
    @Test
    public void hashCode_ShouldReturnSameHashCodeForSameAttribute() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }
    
    @Test
    public void clone_ShouldReturnClonedAttribute() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = attribute1.clone();
        Assert.assertEquals(attribute1, attribute2);
    }

}