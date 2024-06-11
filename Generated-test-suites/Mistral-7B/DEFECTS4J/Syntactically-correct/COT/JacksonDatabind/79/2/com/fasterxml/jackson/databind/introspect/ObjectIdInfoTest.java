package com.example.objectidinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 private PropertyName name;
 private Class<String> scope;
 private Class<MyObjectIdGenerator> generator;
 private Class<SimpleObjectIdResolver> resolver;
 private ObjectIdInfo objectIdInfo;

 @Test
 public void testConstructorWithAllArguments() {
 name = new PropertyName("testPropertyName");
 scope = String.class;
 generator = MyObjectIdGenerator.class;
 resolver = SimpleObjectIdResolver.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);

 assertEquals("testPropertyName", objectIdInfo.getPropertyName().getName());
 assertEquals(String.class, objectIdInfo.getScope());
 assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testConstructorWithNameAndScopeAndGenerator() {
 name = new PropertyName("testPropertyName");
 scope = String.class;
 generator = MyObjectIdGenerator.class;
 objectIdInfo = new ObjectIdInfo(name, scope, generator);

 assertEquals("testPropertyName", objectIdInfo.getPropertyName().getName());
 assertEquals(String.class, objectIdInfo.getScope());
 assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullPropertyName() {
 name = null;
 scope = String.class;
 generator = MyObjectIdGenerator.class;
 resolver = SimpleObjectIdResolver.class;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
 } catch (IllegalArgumentException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullGenerator() {
 name = new PropertyName("testPropertyName");
 scope = String.class;
 generator = null;
 resolver = SimpleObjectIdResolver.class;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
 } catch (IllegalArgumentException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullResolver() {
 name = new PropertyName("testPropertyName");
 scope = String.class;
 generator = MyObjectIdGenerator.class;
 resolver = null;

 try {
 objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
 } catch (IllegalArgumentException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test
 public void testWithAlwaysAsId() {
 boolean alwaysAsId = true;
 objectIdInfo = objectIdInfo.withAlwaysAsId(alwaysAsId);
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 String toString = objectIdInfo.toString();
 assertNotNull(toString);
 }

}