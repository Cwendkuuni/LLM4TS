package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

public class AttributeTest {

 @Test
 public void testCreateAttribute() {
 Attribute attr = new Attribute("key", "value");
 Assert.assertEquals("key", attr.getKey());
 Assert.assertEquals("value", attr.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attributes parent = new Attributes();
 Attribute attr = new Attribute("key", "value", parent);
 parent.put(attr);
 Assert.assertEquals("key", parent.getKey(0));
 Assert.assertEquals("value", parent.getValue(0));
 }

 @Test
 public void testSetAttributeKey() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 Assert.assertEquals("newKey", attr.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attr = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attr = new Attribute("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyValue() {
 Attribute attr = new Attribute("key", "");
 }

 @Test
 public void testHtml() throws IOException {
 Document doc = new Document();
 Attribute attr = new Attribute("key", "value");
 attr.html(doc.appendBody().htmlOutput(true));
 Assert.assertEquals("<key>value</key>", doc.body().html());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attr.getKey());
 Assert.assertEquals("value", attr.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attr = new Attribute("data-test", "test");
 Assert.assertTrue(attr.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document doc = new Document();
 Attribute attr = new Attribute("checked", null);
 Assert.assertTrue(attr.shouldCollapseAttribute(doc.outputSettings()));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attr = new Attribute("checked", null);
 Assert.assertTrue(attr.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 Attribute attr = new Attribute("newAttribute", "value");
 Assert.assertFalse(attr.isBooleanAttribute());
 }

 @Test
 public void testCloneAttribute() {
 Attribute origAttr = new Attribute("key", "value");
 Attribute clonedAttr = origAttr.clone();
 Assert.assertEquals(origAttr, clonedAttr);
 }
}