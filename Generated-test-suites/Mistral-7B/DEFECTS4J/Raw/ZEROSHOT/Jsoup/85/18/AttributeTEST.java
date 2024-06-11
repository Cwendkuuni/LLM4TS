package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setup() {
 attribute = new Attribute("testKey", "testValue");
 attributes = new Attributes();
 attributes.put("testKey", attribute);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attr = new Attribute("testKey", "testValue");
 assertEquals("testKey", attr.getKey());
 assertEquals("testValue", attr.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attr = new Attribute("testKey", "testValue", attributes);
 assertEquals("testKey", attr.getKey());
 assertEquals("testValue", attr.getValue());
 assertEquals(attributes, attr.parent);
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newTestKey");
 assertEquals("newTestKey", attribute.getKey());
 }

 @Test(expected = AssertionError.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 fail("Should not accept null key");
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newTestValue");
 assertEquals("newTestValue", attribute.getValue());
 }

 @Test(expected = AssertionError.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 fail("Should not accept null value");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey=\"testValue\"", sb.toString());
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue("");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey=\"\"", sb.toString());
 }

 @Test
 public void testHtmlWithNullValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue(null);
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey", sb.toString());
 }

 @Test
 public void testToString() {
 assertEquals("testKey=\"testValue\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attr = Attribute.createFromEncoded("testKey", "testValue%20%3D%3D%20testValue");
 assertEquals("testKey", attr.getKey());
 assertEquals("testValue testValue", attr.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithBooleanAttributeKey() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attr1 = new Attribute("testKey", "testValue");
 Attribute attr2 = new Attribute("testKey", "testValue");
 Attribute attr3 = new Attribute("testKey1", "testValue");
 Attribute attr4 = new Attribute("testKey", "testValue1");
 assertTrue(attr1.equals(attr1));
 assertTrue(attr1.equals(attr2));
 assertFalse(attr1.equals(attr3));
 assertFalse(attr1.equals(attr4));
 }

 @Test
 public void testHashCode() {
 Attribute attr1 = new Attribute("testKey", "testValue");
 Attribute attr2 = new Attribute("testKey", "testValue");
 Attribute attr3 = new Attribute("testKey1", "testValue");
 Attribute attr4 = new Attribute("testKey", "testValue1");
 int hashCode1 = attr1.hashCode();
 int hashCode2 = attr2.hashCode();
 int hashCode3 = attr3.hashCode();
 int hashCode4 = attr4.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
 }
}