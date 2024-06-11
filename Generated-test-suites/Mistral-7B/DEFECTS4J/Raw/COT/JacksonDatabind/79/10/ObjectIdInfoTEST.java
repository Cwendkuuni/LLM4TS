package com.example.junit.test.objectidinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

public class ObjectIdInfoTest {

 private static final PropertyName PROPERTY_NAME = new PropertyName("testProperty");
 private static final Class<?> SCOPE = Object.class;
 private static final Class<? extends ObjectIdGenerator<?>> GENERATOR = null; // default value
 private static final Class<? extends ObjectIdResolver> RESOLVER = SimpleObjectIdResolver.class;

 @Test
 public void testConstructorWithPropertyNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(SCOPE, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testConstructorWithPropertyNameStringAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME.getName(), SCOPE, GENERATOR, RESOLVER);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(SCOPE, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testConstructorWithPropertyNameStringScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME.getName(), SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo objectIdInfoWithResolver = new ObjectIdInfo(PROPERTY_NAME.getName(), SCOPE, GENERATOR, RESOLVER, null);
 assertNotNull(objectIdInfo);
 assertNotNull(objectIdInfoWithResolver);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(SCOPE, objectIdInfo.getScope());
 assertNull(objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 assertNull(objectIdInfoWithResolver.getGeneratorType());
 assertSame(RESOLVER, objectIdInfoWithResolver.getResolverType());
 }

 @Test
 public void testConstructorWithPropertyNameStringScopeGeneratorAndResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME.getName(), SCOPE, GENERATOR, RESOLVER, RESOLVER);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(SCOPE, objectIdInfo.getScope());
 assertSame(GENERATOR, objectIdInfo.getGeneratorType());
 assertSame(RESOLVER, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, SCOPE, GENERATOR, RESOLVER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, null, GENERATOR, RESOLVER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, null, RESOLVER);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, null);
 }

 @Test
 public void testWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertNotNull(objectIdInfoWithAlwaysAsId);
 assertFalse(objectIdInfo.getAlwaysAsId());
 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 String toString = objectIdInfo.toString();
 String expectedString = "ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=false";
 assertEquals(expectedString, toString);
 }
}