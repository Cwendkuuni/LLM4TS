package com.example.objectidinfo.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 // Test constructor with valid arguments
 @Test
 public void testObjectIdInfoConstructorValidArgs() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 assertNotNull(objectIdInfo);
 assertEquals("testProp", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.ClassIdGenerator.class, objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test constructor with valid arguments (without PropertyName)
 @Test
 public void testObjectIdInfoConstructorValidArgsWithoutPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProp", Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 assertNotNull(objectIdInfo);
 assertEquals("testProp", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.ClassIdGenerator.class, objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test constructor with valid arguments (without PropertyName and resolver)
 @Test
 public void testObjectIdInfoConstructorValidArgsWithoutPropertyNameAndResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class, false);
 assertNotNull(objectIdInfo);
 assertEquals("testProp", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.ClassIdGenerator.class, objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test constructor with valid arguments (without PropertyName, scope, generator and alwaysAsId)
 @Test
 public void testObjectIdInfoConstructorValidArgsWithoutAllArgs() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), null, null, false, null);
 assertNotNull(objectIdInfo);
 assertEquals("testProp", objectIdInfo.getPropertyName().getName());
 assertNull(objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test constructor with null PropertyName
 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullPropertyName() {
 new ObjectIdInfo(null, Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 }

 // Test constructor with null scope
 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullScope() {
 new ObjectIdInfo(new PropertyName("testProp"), null, ObjectIdGenerator.ClassIdGenerator.class);
 }

 // Test constructor with null generator
 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullGenerator() {
 new ObjectIdInfo(new PropertyName("testProp"), Object.class, null);
 }

 // Test constructor with null resolver
 @Test
 public void testObjectIdInfoConstructorNullResolver() {
 try {
 new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class, false, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test withAlwaysAsId method
 @Test
 public void testWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 // Test getter methods
 @Test
 public void testGetterMethods() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 assertSame(new PropertyName("testProp"), objectIdInfo.getPropertyName());
 assertSame(Object.class, objectIdInfo.getScope());
 assertSame(ObjectIdGenerator.ClassIdGenerator.class, objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.ClassIdGenerator.class);
 String toString = objectIdInfo.toString();
 assertTrue(toString.contains("ObjectIdInfo: propName=testProp"));
 assertTrue(toString.contains("scope=class Object"));
 assertTrue(toString.contains("generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator.ClassIdGenerator"));
 assertFalse(toString.contains("alwaysAsId=true"));
 }
}