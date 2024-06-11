package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Unit tests for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testConstructor() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 assertNotNull(resolver);
 }

 @Test
 public void testGetMechanism() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 try {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 resolver.registerSubtype(String.class, "String");
 fail("Expected exception: not used with class name - based resolvers");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIdFromValue() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 Object value = new HashMap<>();
 String id = resolver.idFromValue(value);
 assertEquals("java.util.HashMap", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 Object value = new HashMap<>();
 Class<?> type = HashMap.class;
 String id = resolver.idFromValueAndType(value, type);
 assertEquals("java.util.HashMap", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 DatabindContext ctxt = MAPPER.getFactory().getPolymorphicTypeValidator();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, ctxt.getTypeFactory());
 String id = "java.util.ArrayList";
 JavaType type = resolver.typeFromId(ctxt, id);
 assertEquals(ArrayList.class, type.getRawClass());
 }

 @Test
 public void testIdFromUnknownClass() throws Exception {
 DatabindContext ctxt = MAPPER.getFactory().getPolymorphicTypeValidator();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, ctxt.getTypeFactory());
 String id = "com.unknown.UnknownClass";
 JavaType type = resolver.typeFromId(ctxt, id);
 assertNull(type);
 }

 @Test
 public void testIdFromInnerClass() throws Exception {
 Class<?> outerType = OuterClass.class;
 Class<?> innerType = OuterClass$InnerClass.class;
 DatabindContext ctxt = MAPPER.getFactory().getPolymorphicTypeValidator();
 ClassNameIdResolver resolver = new ClassNameIdResolver(outerType, ctxt.getTypeFactory());
 String id = "com.fasterxml.jackson.databind.jsontype.impl.OuterClass$InnerClass";
 JavaType type = resolver.typeFromId(ctxt, id);
 assertEquals(innerType, type.getRawClass());
 }

 @Test
 public void testIdFromEnum() throws Exception {
 EnumSet enumSet = EnumSet.of(Color.RED, Color.GREEN);
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, MAPPER.getTypeFactory());
 String id = resolver.idFromValue(enumSet);
 assertEquals("java.util.EnumSet, java.lang.Enum", id);
 }

 @Test
 public void testIdFromListWithArrays() throws Exception {
 Object list = Arrays.asList("one", "two");
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, MAPPER.getTypeFactory());
 String id = resolver.idFromValue(list);
 assertEquals("java.util.ArrayList", id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromInvalidType() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 Object value = new Object();
 String id = resolver.idFromValue(value);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromNullValue() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, MAPPER.getTypeFactory());
 String id = resolver.idFromValue(null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testTypeFromIdWithUnknownClass() throws Exception {
 DatabindContext ctxt = MAPPER.getFactory().getPolymorphicTypeValidator();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, ctxt.getTypeFactory());
 String id = "com.unknown.UnknownClass";
 JavaType type = resolver.typeFromId(ctxt, id);
 fail("Expected ClassNotFoundException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdWithInvalidId() throws Exception {
 DatabindContext ctxt = MAPPER.getFactory().getPolymorphicTypeValidator();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Map.class, ctxt.getTypeFactory());
 String id = "InvalidId";
 JavaType type = resolver.typeFromId(ctxt, id);
 fail("Expected IllegalArgumentException");
 }
}