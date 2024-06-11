package com.example.objectidinfo; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

 private ObjectIdInfo createObjectIdInfo(PropertyName propertyName, Class<?> scope,
         Class<? extends ObjectIdGenerator<?>> generator, boolean alwaysAsId,
         Class<? extends ObjectIdResolver> resolver) {
     return new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
 }

 @Test
 public void testObjectIdInfoConstructor1() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     Class<CustomIdResolver> resolver = CustomIdResolver.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, false, resolver);
     assertNotNull(objectIdInfo);
     assertEquals(propertyName, objectIdInfo.getPropertyName());
     assertEquals(scope, objectIdInfo.getScope());
     assertEquals(generator, objectIdInfo.getGeneratorType());
     assertEquals(resolver, objectIdInfo.getResolverType());
     assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructor2() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, false, null);
     assertNotNull(objectIdInfo);
     assertEquals(propertyName, objectIdInfo.getPropertyName());
     assertEquals(scope, objectIdInfo.getScope());
     assertEquals(generator, objectIdInfo.getGeneratorType());
     assertNull(objectIdInfo.getResolverType());
     assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructor3() {
     String propertyName = "testProperty";
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(new PropertyName(propertyName), scope, generator, false, null);
     assertNotNull(objectIdInfo);
     assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
     assertEquals(scope, objectIdInfo.getScope());
     assertEquals(generator, objectIdInfo.getGeneratorType());
     assertNull(objectIdInfo.getResolverType());
     assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructor4() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     boolean alwaysAsId = true;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, alwaysAsId, SimpleObjectIdResolver.class);
     assertNotNull(objectIdInfo);
     assertEquals(propertyName, objectIdInfo.getPropertyName());
     assertEquals(scope, objectIdInfo.getScope());
     assertEquals(generator, objectIdInfo.getGeneratorType());
     assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
     assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructor5() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     boolean alwaysAsId = true;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);
     assertNotNull(objectIdInfo);
     assertEquals(propertyName, objectIdInfo.getPropertyName());
     assertEquals(scope, objectIdInfo.getScope());
     assertEquals(generator, objectIdInfo.getGeneratorType());
     assertNull(objectIdInfo.getResolverType());
     assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullPropertyName() {
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(null, scope, generator, false, null);
     fail("Should throw NullPointerException, but test passed");
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullScope() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, null, generator, false, null);
     fail("Should throw NullPointerException, but test passed");
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoConstructorNullGenerator() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, null, false, null);
     fail("Should throw NullPointerException, but test passed");
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, false, null);
     ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
     assertNotSame(objectIdInfo, newObjectIdInfo);
     assertTrue(newObjectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
     PropertyName propertyName = new PropertyName("testProperty");
     Class<String> scope = String.class;
     Class<CustomIdGenerator> generator = CustomIdGenerator.class;
     ObjectIdInfo objectIdInfo = createObjectIdInfo(propertyName, scope, generator, false, null);
     String toString = objectIdInfo.toString();
     String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.CustomIdGenerator, alwaysAsId=false";
     assertEquals(expected, toString);
 }

}