package org.jsoup.nodes;

import org.junit.Test;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("test", "value");
        assert attribute.getKey().equals("test");
    }
    
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setKey("newKey");
        assert attribute.getKey().equals("newKey");
    }
    
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("test", "value");
        assert attribute.getValue().equals("value");
    }
    
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("test", "value");
        attribute.setValue("newValue");
        assert attribute.getValue().equals("newValue");
    }
    
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("test", "value");
        assert attribute.html().equals("test=\"value\"");
    }
    
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("test", "value");
        assert attribute.toString().equals("test=\"value\"");
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("test", "amp&amp;");
        assert attribute.getKey().equals("test");
        assert attribute.getValue().equals("amp&");
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "value");
        assert attribute.isDataAttribute();
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("test", "test");
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        assert attribute.shouldCollapseAttribute(out);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "checked");
        assert attribute.isBooleanAttribute();
    }
    
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("test", "value");
        Attribute attribute2 = new Attribute("test", "value");
        assert attribute1.equals(attribute2);
    }
    
    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("test", "value");
        assert attribute.hashCode() != 0;
    }
    
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("test", "value");
        Attribute clonedAttribute = attribute.clone();
        assert clonedAttribute.equals(attribute);
    }
}
