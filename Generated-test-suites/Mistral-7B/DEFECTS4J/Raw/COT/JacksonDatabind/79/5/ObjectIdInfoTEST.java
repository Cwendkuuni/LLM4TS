package com.example.objectidinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

 private ObjectIdInfo createObjectIdInfo(PropertyName name, Class<?> scope, Class<? extends ObjectIdGenerator<?>> gen) {
 return new ObjectIdInfo(name, scope, gen, false);
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyName() {
 ObjectIdInfo objIdInfo = createObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.js.SerializableObjectIdGenerator, alwaysAsId=false", objIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithStringPropertyName() {
 ObjectIdInfo objIdInfo = createObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objIdInfoWithString = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
 assertEquals(objIdInfo, objIdInfoWithString);
 }

 @Test
 public void testObjectIdInfoConstructionWithDeprecatedConstructorWithStringPropertyName() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo("testProperty", Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objIdInfoWithString = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
 assertEquals(objIdInfo, objIdInfoWithString);
 }

 @Test
 public void testObjectIdInfoConstructionDeprecatedConstructorWithPropertyName() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objIdInfoWithPropertyName = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
 assertEquals(objIdInfo, objIdInfoWithPropertyName);
 }

 @Test
 public void testObjectIdInfoConstructionDeprecatedConstructorWithStringPropertyNameAndResolver() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo("testProperty", Object.class, ObjectIdGenerator.class, ObjectIdResolver.class);
 ObjectIdInfo objIdInfoWithPropertyName = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, ObjectIdResolver.class, ObjectIdResolver.class);
 assertEquals(objIdInfo, objIdInfoWithPropertyName);
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameAndAlwaysAsId() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.js.SerializableObjectIdGenerator, alwaysAsId=true", objIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameAndScope() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), CustomScope.class, ObjectIdGenerator.class, false);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.CustomScope, generatorType=class com.fasterxml.jackson.databind.js.SerializableObjectIdGenerator, alwaysAsId=false", objIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameAndGeneratorAndResolver() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, CustomObjectIdGenerator.class, ObjectIdResolver.class, ObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false", objIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objIdInfoWithAlwaysAsId = objIdInfo.withAlwaysAsId(true);
 assertNotEquals(objIdInfo, objIdInfoWithAlwaysAsId);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.js.SerializableObjectIdGenerator, alwaysAsId=true", objIdInfoWithAlwaysAsId.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPropertyName() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullScope() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerator.class, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullGenerator() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullResolver() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, null);
 }

}