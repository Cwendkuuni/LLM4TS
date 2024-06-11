package com.example.objectidinfo; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 private ObjectIdInfo objectIdInfo;
 private PropertyName propertyName;
 private Class<?> scope;
 private Class<? extends ObjectIdGenerator<?>> generator;
 private Class<? extends ObjectIdResolver> resolver;

 @Before
 public void setup() {
 propertyName = new PropertyName("testProperty");
 scope = String.class;
 generator = JsonIdentityInfo.IdentityGenerator.class;
 resolver = SimpleObjectIdResolver.class;
 objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 }

 @Test
 public void testConstructorWithAllArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 assertSame(propertyName, objectIdInfo._propertyName);
 assertSame(scope, objectIdInfo._scope);
 assertSame(generator, objectIdInfo._generator);
 assertSame(resolver, objectIdInfo._resolver);
 assertFalse(objectIdInfo._alwaysAsId);
 }

 @Test
 public void testConstructorWithThreeArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
 assertSame(propertyName, objectIdInfo._propertyName);
 assertSame(scope, objectIdInfo._scope);
 assertSame(generator, objectIdInfo._generator);
 assertFalse(objectIdInfo._alwaysAsId);
 }

 @Test
 public void testConstructorWithTwoArgumentsAndStringName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator);
 assertSame(propertyName, objectIdInfo._propertyName);
 assertSame(scope, objectIdInfo._scope);
 assertSame(generator, objectIdInfo._generator);
 assertFalse(objectIdInfo._alwaysAsId);
 }

 @Test
 public void testConstructorWithFourArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
 assertNotSame(objectIdInfo, newObjectIdInfo);
 assertFalse(newObjectIdInfo._alwaysAsId);
 }

 @Test
 public void testGetters() {
 assertEquals(propertyName, objectIdInfo.getPropertyName());
 assertSame(scope, objectIdInfo.getScope());
 assertSame(generator, objectIdInfo.getGeneratorType());
 assertSame(resolver, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$IdentityGenerator, alwaysAsId=false";
 assertEquals(expected, objectIdInfo.toString());
 }

 @Test
 public void testNullArguments() {
 propertyName = null;
 assertThrows(NullPointerException.class, () -> new ObjectIdInfo(propertyName, scope, generator, false, resolver));

 generator = null;
 assertThrows(NullPointerException.class, () -> new ObjectIdInfo(propertyName, scope, generator, false, resolver));

 resolver = null;
 assertThrows(NullPointerException.class, () -> new ObjectIdInfo(propertyName, scope, generator, false, null));
 }

 @Test
 public void testResolverTypeIsNull() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
 Class<? extends ObjectIdResolver> expected = SimpleObjectIdResolver.class;
 assertSame(expected, objectIdInfo._resolver);
 }

 @Test
 public void testGeneratorTypeIsNull() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, resolver);
 Class<? extends ObjectIdGenerator<?>> expected = null;
 assertSame(expected, objectIdInfo._generator);
 }

 @Test
 public void testScopeTypeIsNull() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator, false, resolver);
 Class<?> expected = null;
 assertSame(expected, objectIdInfo._scope);
 }

 @Test
 public void testAlwaysAsIdWithTrue() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testAlwaysAsIdWithFalse() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testIdGeneratorInstance() {
 ObjectIdGenerator<?> idGenerator = mock(JsonIdentityInfo.IdentityGenerator.class);
 when(idGenerator.generateId(any(Object.class))).thenReturn("testId");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator.getClass(), false, resolver);
 String expectedId = "testId";
 assertEquals(expectedId, objectIdInfo.getIdGenerator().generateId(new Object()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIdGeneratorSetter() {
 objectIdInfo.setGeneratorType(null);
 }

 @Test
 public void testIdResolverInstance() {
 ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
 when(objectIdResolver.idFromValue(any(Object.class))).thenReturn("testId");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, ObjectIdResolver.class);
 String expectedId = "testId";
 assertEquals(expectedId, objectIdInfo.getIdResolver().idFromValue(new Object()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIdResolverSetter() {
 objectIdInfo.setResolverType(null);
 }

}