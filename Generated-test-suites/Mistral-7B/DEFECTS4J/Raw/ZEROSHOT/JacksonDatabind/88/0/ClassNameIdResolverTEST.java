package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private TypeIdResolver classNameIdResolver;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = classNameIdResolver.idFromValue(value);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String.class;
 String id = classNameIdResolver.idFromValueAndType(value, type);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdWithGenericType() throws Exception {
 String id = "java.util.List<java.lang.String>";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 TypeReference<List<String>> expectedType = new TypeReference<List<String>>() {};
 assertEquals(expectedType, javaType.getType());
 }

 @Test
 public void testIdFromValueForEnum() throws Exception {
 Enum e = Enum.valueOf(String.class, "hello");
 String id = classNameIdResolver.idFromValue(e);
 Class<?> expectedClass = String.class;
 Class<?> actualClass = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
 assertEquals(expectedClass, actualClass);
 }

 @Test
 public void testIdFromValueForInnerClass() throws Exception {
 Object outer = new Object();
 Class<?> innerClass = ClassUtil.getCanonicalName(Object.class, "InnerClass");
 Object inner = new InnerClass() {};
 Class<?> expectedClass = innerClass;
 String id = classNameIdResolver.idFromValueAndType(inner, outer.getClass());
 Class<?> actualClass = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id).getRawClass();
 assertEquals(expectedClass, actualClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String id = "invalidId";
 classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 }

}

class InnerClass {
 // Empty inner class for testing purposes
}