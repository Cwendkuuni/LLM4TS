package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
        Assert.assertNull(attributeWithParent.getParent());
    }

    @Test
    public void testAttributeKeySet() {
        Attribute attribute = new Attribute("Key", "Value");
        attribute.setKey("NewKey");
        Assert.assertEquals("NewKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySetNull() {
        Attribute attribute = new Attribute("Key", "Value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSet() {
        Attribute attribute = new Attribute("Key", "Value");
        String newValue = "NewValue";
        Assert.assertEquals("Value", attribute.getValue());
        attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSetNull() {
        Attribute attribute = new Attribute("Key", "Value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        attribute.html(sb2, new Document("").outputSettings().syntax(Document.OutputSettings.Syntax.xml));
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb2));
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "test", "test");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("booleanAttribute", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings, ""));
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings, "false"));
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings, "true"));

        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertFalse(attribute2.shouldCollapseAttribute(outputSettings));
        Assert.assertFalse(attribute2.shouldCollapseAttribute(outputSettings, "value"));
    }

    @Test
    public void testAttributeClone() throws CloneNotSupportedException {
        Attribute original = new Attribute("key", "value");
        Attribute cloned = original.clone();
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getKey(), cloned.getKey());
        Assert.assertEquals(original.getValue(), cloned.getValue());
    }

    @Test
    public void testAttributeMap() {
        Map<String, Attribute> map = new HashMap<>();
        map.put("key1", new Attribute("key1", "value1"));
        map.put("key2", new Attribute("key2", "value2"));

        Attribute attributeFromMap = map.get("key1");
        Assert.assertEquals("value1", attributeFromMap.getValue());

        map.put("key1", new Attribute("key1", "value1_new"));
        Attribute attributeFromMapNew = map.get("key1");
        Assert.assertNotSame(attributeFromMap, attributeFromMapNew);
        Assert.assertEquals("value1_new", attributeFromMapNew.getValue());
    }
}