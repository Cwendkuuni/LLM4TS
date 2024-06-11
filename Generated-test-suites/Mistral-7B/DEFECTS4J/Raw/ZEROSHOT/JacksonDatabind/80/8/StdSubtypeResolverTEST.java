package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

 private static final Class<?> BASE_CLASS = Foo.class;
 private static final Class<?> SUB_CLASS_1 = Sub1.class;
 private static final Class<?> SUB_CLASS_2 = Sub2.class;
 private static final Class<?> SUB_CLASS_3 = Sub3.class;
 private static final Class<?> SUB_CLASS_4 = Sub4.class;
 private static final Class<?> SUB_CLASS_5 = Sub5.class;

 private MapperConfig<?> config;
 private SubtypeResolver resolver;

 @Before
 public void setUp() {
 config = new MapperConfig<>(new AnnotationIntrospector());
 resolver = new StdSubtypeResolver();
 }

 @Test
 public void testRegisterSubtypesByClass() {
 resolver.registerSubtypes(new NamedType(SUB_CLASS_1));
 resolver.registerSubtypes(new NamedType(SUB_CLASS_2));
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(config, null, BASE_CLASS);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SUB_CLASS_1)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_2)));
 }

 @Test
 public void testRegisterSubtypesByInterface() {
 resolver.registerSubtypes(new NamedType(IFoo.class));
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(config, null, BaseInterface.class);
 assertEquals(1, types.size());
 assertTrue(types.contains(new NamedType(IFoo.class)));
 }

 @Test
 public void testRegisterSubtypesByAnnotation() {
 resolver.registerSubtypes(Sub1.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(config, null, BASE_CLASS);
 assertEquals(3, types.size());
 assertTrue(types.contains(new NamedType(BASE_CLASS)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_1)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_3)));
 }

 @Test
 public void testRegisterSubtypesByAnnotationWithName() {
 resolver.registerSubtypes(Sub4.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(config, null, BASE_CLASS);
 assertEquals(4, types.size());
 assertTrue(types.contains(new NamedType(BASE_CLASS)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_1)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_3)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_4, "sub4")));
 }

 @Test
 public void testCollectSubtypesByTypeId() {
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, BASE_CLASS);
 assertEquals(5, types.size());
 assertTrue(types.contains(new NamedType(BASE_CLASS)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_1)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_2)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_3)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_4, "sub4")));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithExplicitName() {
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, IFoo.class);
 assertEquals(1, types.size());
 assertTrue(types.contains(new NamedType(IFoo.class, "IFoo")));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithEmptyList() {
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, String.class);
 assertEquals(0, types.size());
 }

 @Test
 public void testCollectSubtypesByTypeIdWithEmptyList2() {
 resolver.registerSubtypes(new NamedType(SUB_CLASS_5, "sub5"));
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, String.class);
 assertEquals(1, types.size());
 assertTrue(types.contains(new NamedType(String.class, "sub5")));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithCustomName() {
 resolver.registerSubtypes(new NamedType(SUB_CLASS_4, "sub4"));
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, IFoo.class);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(IFoo.class, "IFoo")));
 assertTrue(types.contains(new NamedType(SUB_CLASS_4, "sub4")));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithCustomName2() {
 resolver.registerSubtypes(new NamedType(SUB_CLASS_1, "fooSub1"));
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, BASE_CLASS);
 assertEquals(4, types.size());
 assertTrue(types.contains(new NamedType(BASE_CLASS)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_1, "fooSub1")));
 assertTrue(types.contains(new NamedType(SUB_CLASS_3)));
 assertTrue(types.contains(new NamedType(SUB_CLASS_4, "sub4")));
 }

 @Test
 public void testCollectSubtypesByTypeIdWithCustomName3() {
 resolver.registerSubtypes(new NamedType(SUB_CLASS_1, "fooSub1"));
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(config, null, BASE_CLASS);
 assertEquals(4, types.size());
 Set<String> names = new LinkedHashSet<>();
 for (NamedType type : types) {
 names.add(type.getName());
 }
 assertTrue(names.containsAll(List.of("BASE_CLASS", "SUB_CLASS_1", "fooSub1", "SUB_CLASS_3",
         "SUB_CLASS_4", "sub4")));
 }

 private static class BaseInterface {
 }

 private static class BaseClass {
 }

 private static class Foo {
 }

 private static class Sub1 extends Foo {
 }

 private static class Sub2 extends Foo {
 }

 private static class Sub3 extends Foo {
 }

 private static class Sub4 extends Foo {
 }

 private static class Sub5 extends Object {
 }
}