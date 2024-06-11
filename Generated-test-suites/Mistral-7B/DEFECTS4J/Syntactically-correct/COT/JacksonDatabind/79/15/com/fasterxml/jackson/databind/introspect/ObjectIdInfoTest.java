package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

public class ObjectIdInfoTest {

 private PropertyName name;
 private Class<?> scope;
 private Class<? extends ObjectIdGenerator<?>> generator;
 private Class<? extends ObjectIdResolver> resolver;
 private ObjectIdInfo objectIdInfo;

 @Test
 public void testConstructorWithNameScopeGeneratorResolver() {
 name = new PropertyName("name");
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 resolver = SimpleObjectIdResolver.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

 ObjectIdInfo expected = new ObjectIdInfo(name, scope, generator, resolver, false);
 assertEquals(expected, objectIdInfo);
 }

 @Test
 public void testConstructorWithNameScopeGenerator() {
 name = new PropertyName("name");
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator);

 ObjectIdInfo expected = new ObjectIdInfo(name, scope, generator, false, SimpleObjectIdResolver.class);
 assertEquals(expected, objectIdInfo);
 }

 @Test
 public void testConstructorWithNameScope() {
 name = new PropertyName("name");
 scope = String.class;
 objectIdInfo = new ObjectIdInfo(name, scope, null, false);

 ObjectIdInfo expected = new ObjectIdInfo(name, scope, null, false, SimpleObjectIdResolver.class);
 assertEquals(expected, objectIdInfo);
 }

 @Test
 public void testConstructorWithPropertyNameScopeGeneratorAlwaysAsId() {
 name = new PropertyName("name");
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator, true, resolver);

 ObjectIdInfo expected = new ObjectIdInfo(name, scope, generator, true, resolver);
 assertEquals(expected, objectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullPropertyName() {
 name = null;
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 resolver = SimpleObjectIdResolver.class;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullGenerator() {
 name = new PropertyName("name");
 scope = String.class;
 generator = null;
 resolver = SimpleObjectIdResolver.class;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullResolver() {
 name = new PropertyName("name");
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 resolver = null;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testWithAlwaysAsIdMethod() {
 objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, SimpleObjectIdResolver.class);
 ObjectIdInfo expected = objectIdInfo.withAlwaysAsId(true);
 ObjectIdInfo actual = new ObjectIdInfo(objectIdInfo._propertyName, objectIdInfo._scope, objectIdInfo._generator, true, objectIdInfo._resolver);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPropertyNameMethod() {
 name = new PropertyName("name");
 objectIdInfo = new ObjectIdInfo(name, String.class, CustomObjectIdGenerator.class, SimpleObjectIdResolver.class);
 assertEquals(name, objectIdInfo.getPropertyName());
 }

 @Test
 public void testGetScopeMethod() {
 scope = String.class;
 objectIdInfo = new ObjectIdInfo(new PropertyName("name"), scope, CustomObjectIdGenerator.class, SimpleObjectIdResolver.class);
 assertEquals(scope, objectIdInfo.getScope());
 }

 @Test
 public void testGetGeneratorTypeMethod() {
 generator = CustomObjectIdGenerator.class;
 objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, generator, SimpleObjectIdResolver.class);
 assertEquals(generator, objectIdInfo.getGeneratorType());
 }

 @Test
 public void testGetResolverTypeMethod() {
 resolver = SimpleObjectIdResolver.class;
 objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, resolver);
 assertEquals(resolver, objectIdInfo.getResolverType());
 }

 @Test
 public void testGetAlwaysAsIdMethod() {
 objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, CustomObjectIdGenerator.class, SimpleObjectIdResolver.class);
 boolean expected = false;
 boolean actual = objectIdInfo.getAlwaysAsId();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringMethod() {
 name = new PropertyName("name");
 scope = String.class;
 generator = CustomObjectIdGenerator.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator, SimpleObjectIdResolver.class);
 String expected = "ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false";
 String actual = objectIdInfo.toString();
 assertEquals(expected, actual);
 }
}