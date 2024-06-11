package org.jsoup.nodes;

import org.junit.Test;

public class AttributeTest {
    // Test cases for Attribute class

    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assert attribute.getKey().equals("key");
        assert attribute.getValue().equals("value");
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assert attribute.getKey().equals("newKey");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assert attribute.getValue().equals("newValue");
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assert html.equals("key=\"value\"");
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String str = attribute.toString();
        assert str.equals("key=\"value\"");
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assert attribute.getKey().equals("key");
        assert attribute.getValue().equals("encodedValue");
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        assert isDataAttribute;
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(out);
        assert !shouldCollapseAttribute;
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assert !isBooleanAttribute;
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert attribute1.equals(attribute2);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assert attribute.equals(clonedAttribute);
    }

}