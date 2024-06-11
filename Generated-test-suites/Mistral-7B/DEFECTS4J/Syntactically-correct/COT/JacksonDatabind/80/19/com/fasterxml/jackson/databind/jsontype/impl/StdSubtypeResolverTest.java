package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {

 private static final Class<StdSubtypeResolver> STD_SUBTYPE_RESOLVER_CLASS = StdSubtypeResolver.class;

 @Test
 public void testInstance() {
 StdSubtypeResolver instance = new StdSubtypeResolver();
 assertNotNull(instance);
 }

 @Test
 public void testRegisterSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(new NamedType(Integer.class, "INT"));
 resolver.registerSubtypes(new NamedType(Long.class, "LONG"));
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig<>(), null, null);
 assertEquals(2, subtypes.size());
 }

 @Test
 public void testRegisterSubtypesClasses() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(Integer.class);
 resolver.registerSubtypes(Long.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig<>(), null, null);
 assertEquals(2, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedMember() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 AnnotatedMember property = null; // not used in this test
 JavaType baseType = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedMember() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 AnnotatedMember property = null; // not used in this test
 JavaType baseType = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testCollectAndResolveSubtypesByClassNullConfig() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
 assertNull(subtypes);
 }

 @Test(expected = NullPointerException.class)
 public void testCollectAndResolveSubtypesByTypeIdNullConfig() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
 assertNull(subtypes);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedMemberNullProperty() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 JavaType baseType = null; // not used in this test
 AnnotatedMember property = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedMemberNullBaseType() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 AnnotatedMember property = null; // not used in this test
 JavaType baseType = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedMemberNullProperty() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 AnnotatedMember property = null; // not used in this test
 JavaType baseType = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedMemberNullBaseType() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 Class<?> rawBase = String.class;
 AnnotatedMember property = null; // not used in this test
 JavaType baseType = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedClassNullType() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 AnnotatedClass type = null; // not used in this test
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedClassNullTypeId() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = new MapperConfig<>();
 AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, null);
 assertNotNull(subtypes);
 assertEquals(0, subtypes.size());
 }

}