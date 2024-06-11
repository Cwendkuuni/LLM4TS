package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private TypeFactory typeFactory;
 private TypeIdResolver resolver;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws IOException {
 typeFactory = new MapperFactory().getPolymorphicTypeValidator().getTypeFactory();
 resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 }

 @Test
 public void testGetMechanism() {
 JsonTypeInfo.Id expected = Id.CLASS;
 Id actual = resolver.getMechanism();
 assertEquals(expected, actual);
 }

 @Test
 public void testRegisterSubtype() {
 // not applicable for ClassNameIdResolver
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = resolver.idFromValue(value);
 assertTrue(id.startsWith(value.getClass().getName()));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 String id = resolver.idFromValueAndType(value, String.class);
 assertTrue(id.startsWith(value.getClass().getName()));
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType expected = typeFactory.constructType(String.class);
 JavaType actual = resolver.typeFromId(new Object(), id);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithGenericType() throws Exception {
 String id = "java.util.List<java.lang.String>";
 JavaType expected = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());
 JavaType actual = resolver.typeFromId(new Object(), id);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithEnumSet() throws Exception {
 EnumSet<TestEnum> value = EnumSet.allOf(TestEnum.class);
 String id = typeFactory.constructCollectionType(EnumSet.class, TestEnum.class).toCanonical();
 JavaType expected = typeFactory.constructType(EnumSet.class, TestEnum.class);
 JavaType actual = resolver.typeFromId(new Object(), id);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithEnumMap() throws Exception {
 EnumMap<TestEnum, String> value = new EnumMap<>(TestEnum.class);
 value.put(TestEnum.ONE, "one");
 value.put(TestEnum.TWO, "two");
 String id = typeFactory.constructMapType(EnumMap.class, TestEnum.class, String.class).toCanonical();
 JavaType expected = typeFactory.constructType(EnumMap.class, TestEnum.class, String.class);
 JavaType actual = resolver.typeFromId(new Object(), id);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdWithInvalidId() throws IOException {
 String id = "InvalidTypeId";
 resolver.typeFromId(new Object(), id);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdWithDeserializationContext() throws IOException {
 String id = "no such class found";
 IOException expected = new IOException("no such class found");
 Object context = new Object();
 Object result = resolver.typeFromId(new Object() {
 @Override
 public DeserializationContext getContext() {
 return new Object(); // Placeholder for DeserializationContext
 }
 }, id);
 assertEquals(expected.getMessage(), result.getMessage());
 }

 @Test
 public void testIdFromValueWithEnum() throws Exception {
 TestEnum value = TestEnum.ONE;
 String id = resolver.idFromValue(value);
 assertTrue(id.startsWith(TestEnum.class.getName()));
 }
}

enum TestEnum { ONE, TWO }