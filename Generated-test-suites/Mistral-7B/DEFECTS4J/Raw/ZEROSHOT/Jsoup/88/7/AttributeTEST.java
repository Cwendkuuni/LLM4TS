package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;

public class AttributeTest {

 @ org.junit.BeforeClass
 static void setup() {
 // Add any necessary setup here
 }

 @ org.junit.AfterClass
 static void teardown() {
 // Add any necessary teardown here
 }

 @ org.junit.Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with a valid key and value
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());

 // Test creating a new Attribute with a valid key, value, and parent
 Attributes parentAttributes = new Attributes();
 Attribute attributeWithParent = new Attribute("testKey", "testValue", parentAttributes);
 assertEquals("testKey", attributeWithParent.getKey());
 assertEquals("testValue", attributeWithParent.getValue());
 assertSame(parentAttributes, attributeWithParent.parent);
 }

 @ org.junit.Test
 public void testSettingKey() {
 // Test setting the key of an Attribute
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setKey("newTestKey");
 assertEquals("newTestKey", attribute.getKey());

 // Test setting the key of an Attribute with a parent
 Attribute attributeWithParent = new Attribute("testKey", "testValue", new Attributes());
 attributeWithParent.setKey("newTestKey");
 assertEquals("newTestKey", attributeWithParent.getKey());
 assertNotSame(parentAttributes, attributeWithParent.parent); // new parent should be created
 }

 @ org.junit.Test
 public void testSettingValue() {
 // Test setting the value of an Attribute
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testValue", attribute.getValue());
 attribute.setValue("newTestValue");
 assertEquals("newTestValue", attribute.getValue());

 // Test setting the value of an Attribute with a parent
 Attribute attributeWithParent = new Attribute("testKey", "testValue", new Attributes());
 assertEquals("testValue", attributeWithParent.getValue());
 attributeWithParent.setValue("newTestValue");
 assertEquals("newTestValue", attributeWithParent.getValue());
 assertSame(parentAttributes, attributeWithParent.parent); // no new parent should be created
 }

 @ org.junit.Test
 public void testToString() {
 // Test that toString() returns the HTML representation of the Attribute
 Attribute attribute = new Attribute("testKey", "testValue");
 assertEquals("testKey=\"testValue\"", attribute.toString());
 }

 @ org.junit.Test
 public void testCreateFromEncoded() {
 // Test creating a new Attribute from an encoded key and value
 Attribute attribute = Attribute.createFromEncoded("testKey", "testValue%20=%22test%22");
 assertEquals("testKey", attribute.getKey());
 assertEquals("test test", attribute.getValue());
 }

 @ org.junit.Test
 public void testIsDataAttribute() {
 // Test that isDataAttribute() returns true for data attributes
 Attribute dataAttribute = new Attribute("data-test", "test");
 assertTrue(dataAttribute.isDataAttribute());

 // Test that isDataAttribute() returns false for non-data attributes
 Attribute nonDataAttribute = new Attribute("test", "test");
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @ org.junit.Test
 public void testShouldCollapseAttribute() {
 // Test that shouldCollapseAttribute() returns true for collapsible boolean attributes
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(new Attribute("checked", null).shouldCollapseAttribute(outputSettings));
 assertTrue(new Attribute("selected", null).shouldCollapseAttribute(outputSettings));

 // Test that shouldCollapseAttribute() returns false for non-collapsible boolean attributes
 assertFalse(new Attribute("type", "text").shouldCollapseAttribute(outputSettings));
 assertFalse(new Attribute("src", "image.jpg").shouldCollapseAttribute(outputSettings));
 }

 @ org.junit.Test
 public void testIsBooleanAttribute() {
 // Test that isBooleanAttribute() returns true for Boolean attributes
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());

 // Test that isBooleanAttribute() returns false for non-Boolean attributes
 Attribute nonBooleanAttribute = new Attribute("type", "text");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @ org.junit.Test
 public void testEquals() {
 // Test that equals() compares keys and values
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey2", "testValue2");
 Attribute attribute3 = new Attribute("testKey1", "testValue1");
 Attribute attribute4 = new Attribute("testKey1", "testValue2");
 assertTrue(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(new Object()));

 // Test that equals() does not consider parents
 Attribute attribute5 = new Attribute("testKey1", "testValue1", new Attributes());
 Attribute attribute6 = new Attribute("testKey1", "testValue1");
 assertSame(attribute5, attribute6); // equals() should return the same object
 }

 @ org.junit.Test
 public void testHashCode() {
 // Test that hashCode() returns consistent results
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey2", "testValue2");
 Attribute attribute3 = new Attribute("testKey1", "testValue1");
 Attribute attribute4 = new Attribute("testKey1", "testValue2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 int hashCode4 = attribute4.hashCode();
 assertTrue(hashCode1 == hashCode3);
 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode4);
 }

 @ org.junit.Test
 public void testClone() {
 // Test that clone() returns a deep copy of the Attribute
 Attribute attribute = new Attribute("testKey", "testValue");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertNotSame(attribute.parent, clonedAttribute.parent);
 }
}