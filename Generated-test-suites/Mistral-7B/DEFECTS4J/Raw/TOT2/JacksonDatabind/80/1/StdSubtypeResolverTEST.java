package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

 // Test for registerSubtypes method
 @Test
 public void testRegisterSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> baseType = Object.class;
 Class<?>[] subtypes = new Class[] {String.class, Integer.class};
 resolver.registerSubtypes(subtypes);
 assertNotNull(resolver._registeredSubtypes);
 assertEquals(2, resolver._registeredSubtypes.size());
 }

 // Test for collectAndResolveSubtypesByClass method with a single subtype
 @Test
 public void testCollectAndResolveSubtypesByClass_singleSubtype() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedMember property = new AnnotatedMember(Integer.class, "someProperty");
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 AnnotatedClass subtypeClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
 NamedType expectedSubtype = new NamedType(Integer.class, "java.lang.Integer");
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Did not get expected subtype", expectedSubtype, actualSubtypes.iterator().next());
 }

 // Test for collectAndResolveSubtypesByClass method with multiple subtypes
 @Test
 public void testCollectAndResolveSubtypesByClass_multipleSubtypes() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedMember property = new AnnotatedMember(Object.class, "someProperty");
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 AnnotatedClass subtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
 AnnotatedClass subtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
 NamedType expectedSubtype1 = new NamedType(String.class, "java.lang.String");
 NamedType expectedSubtype2 = new NamedType(Integer.class, "java.lang.Integer");
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Unexpected number of subtypes", 2, actualSubtypes.size());
 // Verify subtypes in the expected order
 iterateCollection(actualSubtypes, (NamedType subtype, int index) -> {
 if (index == 0) {
 assertEquals("Unexpected subtype", expectedSubtype1, subtype);
 } else {
 assertEquals("Unexpected subtype", expectedSubtype2, subtype);
 }
 });
 }

 // Test for collectAndResolveSubtypesByTypeId method with a single subtype
 @Test
 public void testCollectAndResolveSubtypesByTypeId_singleSubtype() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedMember property = new AnnotatedMember(Integer.class, "someProperty");
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 AnnotatedClass subtypeClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Did not get expected subtype", 1, actualSubtypes.size());
 // Verify subtype
 iterateCollection(actualSubtypes, (NamedType subtype) -> {
 NamedType expectedSubtype = new NamedType(Integer.class, "java.lang.Integer");
 assertEquals("Unexpected subtype", expectedSubtype, subtype);
 });
 }

 // Test for collectAndResolveSubtypesByTypeId method with multiple subtypes
 @Test
 public void testCollectAndResolveSubtypesByTypeId_multipleSubtypes() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Unexpected number of subtypes", 2, actualSubtypes.size());
 // Verify subtypes in the expected order
 iterateCollection(actualSubtypes, (NamedType subtype, int index) -> {
 if (index == 0) {
 Class<?> type = subtype.getType();
 assertTrue("Unexpected type", type.isInstance(String.class));
 } else {
 Class<?> type = subtype.getType();
 assertTrue("Unexpected type", type.isInstance(Integer.class));
 }
 });
 }

 // Test for handling abstract classes
 @Test
 public void testCollectAndResolveSubtypesByTypeId_abstractClass() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals("Unexpected number of subtypes", 0, actualSubtypes.size());
 }

 // Test for handling null base type
 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeId_nullBaseType() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
 fail("Should have thrown exception");
 }

 // Test for handling null property
 @Test
 public void testCollectAndResolveSubtypesByTypeId_nullProperty() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Unexpected number of subtypes", 0, actualSubtypes.size());
 }

 // Test for handling null config
 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeId_nullConfig() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 AnnotatedMember property = new AnnotatedMember(Integer.class, "someProperty");
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
 fail("Should have thrown exception");
 }

 // Test for handling null property type
 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeId_nullPropertyType() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedMember property = new AnnotatedMember(Object.class, "someProperty");
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, null);
 fail("Should have thrown exception");
 }

 // Test for handling null property name
 @Test
 public void testCollectAndResolveSubtypesByTypeId_nullPropertyName() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig();
 AnnotatedMember property = new AnnotatedMember(Object.class, null);
 JavaType baseType = config.getTypeFactory().constructType(Object.class);
 Collection<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
 assertNotNull(actualSubtypes);
 assertEquals("Unexpected number of subtypes", 0, actualSubtypes.size());
 }
}