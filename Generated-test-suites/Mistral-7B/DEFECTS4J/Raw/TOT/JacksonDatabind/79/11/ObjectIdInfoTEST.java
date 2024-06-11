package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * Unit tests for {@link ObjectIdInfo} class
 */
public class ObjectIdInfoTest {

 // Test case for constructor with PropertyName, scope, ObjectIdGenerator and ObjectIdResolver
 @Test
 public void testObjectIdInfoWithPropertyNameAndAnnotations() {
  PropertyName propertyName = new PropertyName("id");
  Class<String> scope = String.class;
  Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
  Class<MyCustomObjectIdResolver> objectIdResolver = MyCustomObjectIdResolver.class;
  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, objectIdResolver);
  assertEquals(propertyName, objectIdInfo.getPropertyName());
  assertEquals(scope, objectIdInfo.getScope());
  assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
  assertEquals(objectIdResolver, objectIdInfo.getResolverType());
  assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test case for constructor with PropertyName, scope, ObjectIdGenerator
 @Test
 public void testObjectIdInfoWithPropertyNameAndObjectIdGenerator() {
  PropertyName propertyName = new PropertyName("id");
  Class<String> scope = String.class;
  Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);
  assertEquals(propertyName, objectIdInfo.getPropertyName());
  assertEquals(scope, objectIdInfo.getScope());
  assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
  assertNull(objectIdInfo.getResolverType());
  assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test case for constructor with PropertyName, scope, ObjectIdGenerator and alwaysAsId = true
 @Test
 public void testObjectIdInfoWithPropertyNameAndObjectIdGeneratorWithAlwaysAsId() {
  PropertyName propertyName = new PropertyName("id");
  Class<String> scope = String.class;
  Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, true);
  assertEquals(propertyName, objectIdInfo.getPropertyName());
  assertEquals(scope, objectIdInfo.getScope());
  assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
  assertNull(objectIdInfo.getResolverType());
  assertTrue(objectIdInfo.getAlwaysAsId());
 }

 // Test case for constructor with String, scope, ObjectIdGenerator and ObjectIdResolver
 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithStringAndAnnotations() {
  String name = "id";
  Class<String> scope = String.class;
  Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
  Class<MyCustomObjectIdResolver> objectIdResolver = MyCustomObjectIdResolver.class;
  try {
   ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, objectIdGenerator, objectIdResolver);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("Cannot instantiate ObjectIdInfo with String name: " + name, e.getMessage());
  }
 }

 // Test case for constructor with String, scope, ObjectIdGenerator
 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithStringAndObjectIdGenerator() {
  String name = "id";
  Class<String> scope = String.class;
  Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
  try {
   ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, objectIdGenerator);
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("Cannot instantiate ObjectIdInfo with String name: " + name, e.getMessage());
  }
 }

}

class MyCustomObjectIdGenerator implements ObjectIdGenerator<Object> {
 // Implementation of ObjectIdGenerator interface
}

class MyCustomObjectIdResolver implements ObjectIdResolver {
 // Implementation of ObjectIdResolver interface
}