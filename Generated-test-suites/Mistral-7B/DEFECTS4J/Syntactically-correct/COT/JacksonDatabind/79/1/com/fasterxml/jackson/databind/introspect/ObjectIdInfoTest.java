package com.example.test.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 private PropertyName name;
 private Class<?> scope;
 private Class<? extends ObjectIdGenerator<?>> generator;
 private Class<? extends ObjectIdResolver> resolver;
 private ObjectIdInfo objectIdInfo;

 @Test
 public void testObjectIdInfoConstructionWithAllArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
 assertNotNull(objectIdInfo);
 assertEquals("propertyName", objectIdInfo.getPropertyName().getId());
 assertEquals(MyScope.class, objectIdInfo.getScope());
 assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, null);
 assertNotNull(objectIdInfo);
 assertEquals("propertyName", objectIdInfo.getPropertyName().getId());
 assertEquals(MyScope.class, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameStringAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, null);
 ObjectIdInfo objectIdInfoWithNameString = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, null, false, null);
 assertNotNull(objectIdInfo);
 assertNotNull(objectIdInfoWithNameString);
 assertEquals(objectIdInfo, objectIdInfoWithNameString);
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameStringScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, MyObjectIdGenerator.class, null);
 ObjectIdInfo objectIdInfoWithNameStringAndGenerator = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, MyObjectIdGenerator.class, false, null);
 assertNotNull(objectIdInfo);
 assertNotNull(objectIdInfoWithNameStringAndGenerator);
 assertEquals(objectIdInfo, objectIdInfoWithNameStringAndGenerator);
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, MyObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertNotNull(objectIdInfoWithAlwaysAsId);
 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, MyScope.class, MyObjectIdGenerator.class, false);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), null, MyObjectIdGenerator.class, false);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, null, false);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propertyName"), MyScope.class, MyObjectIdGenerator.class, false, null);
 }

}