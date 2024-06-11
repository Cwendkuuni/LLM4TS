package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String id = resolver.idFromValue(value);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String.class;
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String id = resolver.idFromValueAndType(value, type);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.util.ArrayList";
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 JavaType javaType = resolver.typeFromId(null, id);
 assertEquals(ArrayList.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdWithGenericClass() throws Exception {
 String id = "java.util.ArrayList<java.lang.String>";
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 JavaType javaType = resolver.typeFromId(null, id);
 TypeReference<List<String>> expectedType = new TypeReference<List<String>>() {};
 assertEquals(expectedType, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String id = "invalidTypeId";
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 resolver.typeFromId(null, id);
 }

 @Test(expected = IOException.class)
 public void testClassNotFoundException() throws Exception {
 String id = "com.non.existent.package.NonExistentClass";
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 resolver.typeFromId(null, id);
 }

 @Test
 public void testEnumClass() throws Exception {
 Enum testEnum = Enum.values()[0];
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 ObjectMapper mapper = new ObjectMapper();
 String id = resolver.idFromValue(testEnum);
 JavaType javaType = resolver.typeFromId(null, id);
 assertEquals(testEnum.getClass(), javaType.getRawClass());
 }

 @Test
 public void testInnerClass() throws Exception {
 Class<?> outerClass = String[].class;
 Class<?> innerClass = IntSummaryStatistics.class;
 ClassNameIdResolver resolver = new ClassNameIdResolver(outerClass.getName(), objectMapper.getTypeFactory());
 ObjectMapper mapper = new ObjectMapper();
 String id = resolver.idFromValue(new IntSummaryStatistics());
 JavaType javaType = resolver.typeFromId(null, id);
 assertEquals(innerClass, javaType.getRawClass());
 }

 @Test
 public void testArrayClass() throws Exception {
 Object value = new int[5];
 ClassNameIdResolver resolver = new ClassNameIdResolver(int[].class.getTypeName(), objectMapper.getTypeFactory());
 String id = resolver.idFromValue(value);
 assertEquals("java.lang.int[]", id);
 }
}