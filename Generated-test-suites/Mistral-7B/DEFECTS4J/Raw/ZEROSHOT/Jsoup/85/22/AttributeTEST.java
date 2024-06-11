package org.jsoup.nodes.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;

public class AttributeTest {

 @org.junit.BeforeClass
 static void setup() {
 // Set up any static variables or objects needed for testing
 }

 @org.junit.AfterClass
 static void tearDown() {
 // Clean up any static variables or objects created during testing
 }

 @org.junit.Before
 static void beforeEach() {
 // Set up any variables or objects needed for each test
 }

 @org.junit.After
 static void afterEach() {
 // Clean up any variables or objects created during each test
 }

 @org.junit.Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with unencoded key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Test creating a new Attribute with unencoded key, value, and parent
 Attribute attributeParent = new Attributes();
 Attribute attributeWithParent = new Attribute("key", "value", attributeParent);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertEquals(attributeWithParent, attributeParent.put("key", "value"));

 // Test creating a new Attribute from encoded key and value
 Attribute encodedAttribute = Attribute.createFromEncoded("key", "value=%61%6D%65%20%61%73%73%6F%75%72%73%65%64");
 assertEquals("key", encodedAttribute.getKey());
 assertEquals("value", encodedAttribute.getValue());
 }

 @org.junit.Test
 public void testAttributeKey() {
 // Test setting and getting the attribute key
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @org.junit.Test
 public void testAttributeValue() {
 // Test setting and getting the attribute value
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @org.junit.Test
 public void testAttributeHtml() {
 // Test generating the HTML representation of the attribute
 Attribute attribute = new Attribute("key", "value");
 StringBuilder expectedHtml = new StringBuilder();
 Attribute.html("key", "value", expectedHtml, new Document.OutputSettings());
 String actualHtml = attribute.html();
 assertEquals(expectedHtml.toString(), actualHtml);
 }

 @org.junit.Test
 public void testShouldCollapseAttribute() {
 // Test whether the attribute should be collapsed based on the output settings and key/value
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute attributeBoolean = new Attribute("boolean", "");
 assertTrue(attributeBoolean.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute attributeBooleanValue = new Attribute("boolean", "boolean");
 assertFalse(attributeBooleanValue.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute attributeNonBoolean = new Attribute("nonBoolean", "value");
 assertFalse(attributeNonBoolean.shouldCollapseAttribute(new Document.OutputSettings()));
 }

 @org.junit.Test
 public void testIsBooleanAttribute() {
 // Test determining if the attribute is a boolean attribute
 Attribute booleanAttribute = new Attribute("boolean", "");
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("nonBoolean", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @org.junit.Test
 public void testEquals() {
 // Test the equals method
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @org.junit.Test
 public void testHashCode() {
 // Test the hashCode method
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertTrue(hashCode1 == hashCode2); // equals should return same hashcode
 assertNotEquals(hashCode1, hashCode3); // different keys or values should result in different hashcodes
 }

 @org.junit.Test
 public void testClone() {
 // Test cloning an attribute
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}