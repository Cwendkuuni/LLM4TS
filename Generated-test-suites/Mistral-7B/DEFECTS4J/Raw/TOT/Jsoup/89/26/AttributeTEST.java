package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);

 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 String key = "id";
 String value = "elementId";
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(key, value, parent);

 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
Assert.assertEquals(1, parent.size());
 Assert.assertEquals(key, parent.keyAt(0));
 Assert.assertEquals(value, parent.valueAt(0));
 }

 @Test
 public void testAttributeKeySetter() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);

 attribute.setKey("newKey");

 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeKeySetterWithNullKey() {
 String key = null;
 String value = "active";
 Attribute attribute = new Attribute(key, value);

 attribute.setKey("newKey");
 }

 @Test
 public void testAttributeValueSetter() {
 String key = "class";
 String oldValue = "active";
 String newValue = "newClass";
 Attribute attribute = new Attribute(key, oldValue);

 attribute.setValue(newValue);

 Assert.assertEquals(newValue, attribute.getValue());
 Assert.assertEquals(oldValue, attribute.getOldValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeValueSetterWithNullValue() {
 String key = "class";
 String oldValue = "active";
 String newValue = null;
 Attribute attribute = new Attribute(key, oldValue);

 attribute.setValue(newValue);
 }

 @Test
 public void testAttributeHtml() throws IOException {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);

 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());

 Assert.assertEquals("class=\"active\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeIsDataAttribute() {
 String key = "data-test";
 Attribute attribute = new Attribute(key, null);

 Assert.assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testAttributeIsDataAttributeWithNonDataAttributeKey() {
 String key = "class";
 Attribute attribute = new Attribute(key, null);

 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testAttributeShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);

 String booleanAttribute = "checked";
 String nonBooleanAttribute = "data-test";
 String emptyValue = "";
 String sameValueAsKey = "class";
 Attribute booleanAttributeAttribute = new Attribute(booleanAttribute, null);
 Attribute emptyValueAttribute = new Attribute(emptyValue, null);
 Attribute sameValueAsKeyAttribute = new Attribute(sameValueAsKey, sameValueAsKey);

 Assert.assertTrue(booleanAttributeAttribute.shouldCollapseAttribute(outputSettings));
 Assert.assertFalse(booleanAttributeAttribute.shouldCollapseAttribute(""));
 Assert.assertFalse(booleanAttributeAttribute.shouldCollapseAttribute(null));
 Assert.assertFalse(emptyValueAttribute.shouldCollapseAttribute(outputSettings));
 Assert.assertFalse(emptyValueAttribute.shouldCollapseAttribute(""));
 Assert.assertFalse(emptyValueAttribute.shouldCollapseAttribute(null));
 Assert.assertFalse(sameValueAsKeyAttribute.shouldCollapseAttribute(outputSettings));
 Assert.assertFalse(sameValueAsKeyAttribute.shouldCollapseAttribute(""));
 Assert.assertFalse(sameValueAsKeyAttribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testAttributeIsBooleanAttribute() {
 String booleanAttribute = "checked";Assert.assertFalse(new Attribute(booleanAttribute, null).isBooleanAttribute());
 String nonBooleanAttribute = "class";
 Attribute attribute = new Attribute(nonBooleanAttribute, null);

 Assert.assertFalse(attribute.isBooleanAttribute());
 }
}