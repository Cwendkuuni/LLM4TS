package com.example.objectidinfo; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

public class ObjectIdInfoTest {

 private static final PropertyName TEST_PROPERTY_NAME = new PropertyName("testProperty");
 private static final Class<Integer> TEST_SCOPE = Integer.class;
 private static final Class<MyCustomObjectIdGenerator> TEST_GENERATOR = MyCustomObjectIdGenerator.class;
 private static final Class<MyCustomObjectIdResolver> TEST_RESOLVER = MyCustomObjectIdResolver.class;

 @Test
 public void testObjectIdInfoConstructionWithAllParams() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(TEST_PROPERTY_NAME, TEST_SCOPE, TEST_GENERATOR, TEST_RESOLVER);

 assertEquals("PropertyName: testProperty, scope: class int, generatorType: class com.example.MyCustomObjectIdGenerator, alwaysAsId: false",
 objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithOnlyNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(TEST_PROPERTY_NAME, TEST_SCOPE);

 assertEquals("PropertyName: testProperty, scope: class int, generatorType: class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver, alwaysAsId: false",
 objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructionWithOnlyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", TEST_SCOPE);

 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructionWithOnlyNameAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", TEST_SCOPE, TEST_GENERATOR);

 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(TEST_PROPERTY_NAME, TEST_SCOPE, TEST_GENERATOR, true);

 assertEquals("PropertyName: testProperty, scope: class int, generatorType: class com.example.MyCustomObjectIdGenerator, alwaysAsId: true",
 objectIdInfo.toString());

 ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(false);

 assertEquals("PropertyName: testProperty, scope: class int, generatorType: class com.example.MyCustomObjectIdGenerator, alwaysAsId: false",
 withAlwaysAsId.toString());
 }

 @Test
 public void testGetters() {
 PropertyName propertyName = TEST_PROPERTY_NAME;
 Class<Integer> scope = TEST_SCOPE;
 Class<MyCustomObjectIdGenerator> generator = TEST_GENERATOR;
 Class<MyCustomObjectIdResolver> resolver = TEST_RESOLVER;
 boolean alwaysAsId = false;

 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);

 assertEquals(propertyName, objectIdInfo.getPropertyName());
 assertEquals(scope, objectIdInfo.getScope());
 assertEquals(generator, objectIdInfo.getGeneratorType());
 assertEquals(resolver, objectIdInfo.getResolverType());
 assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
 }

}