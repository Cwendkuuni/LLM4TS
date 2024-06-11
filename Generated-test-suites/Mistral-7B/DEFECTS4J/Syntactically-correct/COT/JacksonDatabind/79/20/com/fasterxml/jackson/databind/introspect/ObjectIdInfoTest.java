package com.example.objectidinfo; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

public class ObjectIdInfoTest {

 private static final PropertyName PROPERTY_NAME = new PropertyName("exampleProperty");
 private static final Class<?> SCOPE = String.class;
 private static final Class<? extends ObjectIdGenerator<?>> GENERATOR = ObjectIdGenerator.class;
 private static final Class<? extends ObjectIdResolver> RESOLVER = ObjectIdResolver.class;

 @Test
 public void testObjectIdInfoWithAllArgs() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER, null);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoWithPropertyNameAndScopeArgs() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, false, null);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, SCOPE, GENERATOR);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, null, GENERATOR);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, true, RESOLVER);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, true, RESOLVER);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoWithCustomResolver() {
 Class<? extends ObjectIdResolver> customResolver = CustomObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, false, customResolver);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, false, customResolver);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoWithToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, false, RESOLVER);
 String expectedToString = "ObjectIdInfo: propName=exampleProperty,"
 + " scope=class java.lang.String,"
 + " generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator,"
 + " alwaysAsId=false";
 String actualToString = objectIdInfo.toString();
 assertEquals(expectedToString, actualToString);
 }

}