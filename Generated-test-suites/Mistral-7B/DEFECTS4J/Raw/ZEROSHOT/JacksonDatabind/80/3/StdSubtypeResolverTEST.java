package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.util.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

 private StdSubtypeResolver resolver;
 private AnnotationIntrospector annotationIntrospector;
 private MapperConfig<?> config;

 @Before
 public void setup() {
 resolver = new StdSubtypeResolver();
 annotationIntrospector = new AnnotationIntrospector();
 config = new MapperConfig<>();
 }

 @Test
 public void testRegisterSubtypesByClass() {
 Class<?> superClass = Integer.class;
 Class<?> subclass1 = Integer.TYPE;
 Class<?> subclass2 = Long.TYPE;

 resolver.registerSubtypes(subclass1, subclass2);
 JavaType superType = config.getTypeFactory().constructType(superClass);
 AnnotatedMember member = new AnnotatedMember(null, null, null, null, null, null);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, member, superType);

 assertEquals(2, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testRegisterSubtypesByAnnotation() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subclass1);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);

 assertEquals(2, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testRegisterSubtypesByAnnotationAndClass() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 resolver.registerSubtypes(AppleSubtype.class);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subclass1);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);

 assertEquals(3, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 assertEquals(AppleSubtype.class.getName(), subtypes.get(2).getName());
 }

 @Test
 public void testNoDuplicateRegistration() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 resolver.registerSubtypes(subclass1);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subclass1);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);

 assertEquals(2, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testRegisterSubtypesByAnnotationWithSameName() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;
 Class<?> subclass3 = Apple.class;

 resolver.registerSubtypes(subclass1, subclass2);
 resolver.registerSubtypes(subclass3);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subclass1);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);

 assertEquals(3, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 assertEquals(subclass3.getName(), subtypes.get(2).getName());
 }

 @Test
 public void testCollectSubtypesByTypeId() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superClass);
 JavaType baseType = config.getTypeFactory().constructType(superClass);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

 assertEquals(2, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testCollectSubtypesByTypeIdWithExplicitRegistration() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;
 Class<?> subclass3 = AppleSubtype.class;

 resolver.registerSubtypes(subclass1, subclass2);
 resolver.registerSubtypes(subclass3);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superClass);
 JavaType baseType = config.getTypeFactory().constructType(superClass);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

 assertEquals(3, subtypes.size());
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 assertEquals(subclass3.getName(), subtypes.get(2).getName());
 }

 @Test
 public void testRegisterSubtypesWithCustomName() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subclass1);
 JavaType baseType = config.getTypeFactory().constructType(superClass);

 AnnotationIntrospector.FindSubtypes findSubtypes = annotationIntrospector.findAnnotation(annotatedClass.getAnnotations(), JsonTypeInfo.class);
 String id = findSubtypes.getId();

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

 assertEquals(2, subtypes.size());
 assertTrue(id.equals("fruit"));
 assertEquals(subclass1.getName(), subtypes.get(0).getName());
 assertEquals(subclass2.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testCollectSubtypesByTypeIdWithCustomNameRegistration() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;
 Class<?> subclass3 = AppleSubtype.class;

 resolver.registerSubtypes(subclass1, subclass2);
 resolver.registerSubtypes(subclass3, "fruit");
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superClass);
 JavaType baseType = config.getTypeFactory().constructType(superClass);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

 assertEquals(3, subtypes.size());
 assertTrue(subclass1.getName().equals("apple") || subclass1.equals(subclass3));
 assertTrue(subclass2.getName().equals("orange"));
 assertTrue(subclass3.getName().equals("apple"));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithTypeIdMissing() {
 Class<?> superClass = Object.class;
 Class<?> subclass1 = Apple.class;
 Class<?> subclass2 = Orange.class;

 resolver.registerSubtypes(subclass1, subclass2);
 AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superClass);
 JavaType baseType = config.getTypeFactory().constructType(superClass);

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

 assertTrue(subtypes.isEmpty());
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "name")
 @JsonTypeName("apple")
 static class Apple {
 }

 @JsonTypeName("orange")
 static class AppleSubtype extends Apple {
 }

 static class Orange {
 }
}