package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final String PACKAGE_NAME = "com.fasterxml.jackson.databind.jsontype.impl";

 @org.junit.BeforeClass
 public static void setup() {
 // Initialize Jackson ObjectMapper for test
 ObjectMapper objectMapper = new ObjectMapper();
 }

 /**
 * Test for idFromValue method with primitive types.
 */
 @org.junit.Test
 public void testIdFromValuePrimitive() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());

 // When
 String id = classResolver.idFromValue((byte) 1);
 String idInt = classResolver.idFromValue(1);
 String idShort = classResolver.idFromValue((short) 1);
 String idIntLong = classResolver.idFromValue((int) 1);
 String idLong = classResolver.idFromValue(1L);
 String idFloat = classResolver.idFromValue((float) 1.0F);
 String idDouble = classResolver.idFromValue(1.0D);
 String idBoolean = classResolver.idFromValue(true);
 String idChar = classResolver.idFromValue('A');

 // Then
 assertEquals("byte", id);
 assertEquals("int", idInt);
 assertEquals("int", idIntLong); // int and long are the same for idFromValue
 assertEquals("int", idShort); // short is promoted to int
 assertEquals("double", idFloat);
 assertEquals("double", idDouble);
 assertEquals("boolean", idBoolean);
 assertEquals("char", idChar);
 }

 /**
 * Test for idFromValue method with String.
 */
 @org.junit.Test
 public void testIdFromValueString() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());

 // When
 String idString = classResolver.idFromValue("Hello World");

 // Then
 assertEquals("java.lang.String", idString);
 }

 /**
 * Test for idFromValue method with Enum.
 */
 @org.junit.Test
 public void testIdFromValueEnum() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 Enum testEnum = Enum.valueOf(TestEnum.class, "TEST_VALUE");
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());

 // When
 String idEnum = classResolver.idFromValue(testEnum);

 // Then
 assertEquals(TestEnum.class.getName(), idEnum);
 }

 /**
 * Test for idFromValueAndType method with primitive types.
 */
 @org.junit.Test
 public void testIdFromValueAndTypePrimitive() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Integer.TYPE, objectMapper.getTypeFactory());

 // When
 String idInt = classResolver.idFromValueAndType(1, Integer.TYPE);
 String idLong = classResolver.idFromValueAndType(1L, Long.TYPE);
 String idByte = classResolver.idFromValueAndType((byte) 1, Byte.TYPE);
 String idShort = classResolver.idFromValueAndType((short) 1, Short.TYPE);
 String idFloat = classResolver.idFromValueAndType((float) 1.0F, Float.TYPE);
 String idDouble = classResolver.idFromValueAndType(1.0D, Double.TYPE);
 String idBoolean = classResolver.idFromValueAndType(true, Boolean.TYPE);
 String idChar = classResolver.idFromValueAndType('A', Character.TYPE);

 // Then
 assertEquals("int", idInt);
 assertEquals("long", idLong);
 assertEquals("byte", idByte);
 assertEquals("short", idShort);
 assertEquals("float", idFloat);
 assertEquals("double", idDouble);
 assertEquals("boolean", idBoolean);
 assertEquals("char", idChar);
 }

 /**
 * Test for idFromValueAndType method with String.
 */
 @org.junit.Test
 public void testIdFromValueAndTypeString() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());

 // When
 String idString = classResolver.idFromValueAndType("Hello World", String.class);

 // Then
 assertEquals("java.lang.String", idString);
 }

 /**
 * Test for idFromValueAndType method with Enum.
 */
 @org.junit.Test
 public void testIdFromValueAndTypeEnum() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 Enum testEnum = Enum.valueOf(TestEnum.class, "TEST_VALUE");

 // When
 String idEnum = classResolver.idFromValueAndType(testEnum, TestEnum.class);

 // Then
 assertEquals(TestEnum.class.getName(), idEnum);
 }

 /**
 * Test for typeFromId method with basic types.
 */
 @org.junit.Test
 public void testTypeFromIdBasicTypes() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idByte = "byte";
 String idInt = "int";
 String idShort = "short";
 String idLong = "long";
 String idFloat = "float";
 String idDouble = "double";
 String idBoolean = "boolean";
 String idChar = "char";
 String idString = "java.lang.String";
 String idEnum = TestEnum.class.getName();

 // When
 JavaType javaTypeByte = classResolver.typeFromId(null, idByte);
 JavaType javaTypeInt = classResolver.typeFromId(null, idInt);
 JavaType javaTypeShort = classResolver.typeFromId(null, idShort);
 JavaType javaTypeLong = classResolver.typeFromId(null, idLong);
 JavaType javaTypeFloat = classResolver.typeFromId(null, idFloat);
 JavaType javaTypeDouble = classResolver.typeFromId(null, idDouble);
 JavaType javaTypeBoolean = classResolver.typeFromId(null, idBoolean);
 JavaType javaTypeChar = classResolver.typeFromId(null, idChar);
 JavaType javaTypeString = classResolver.typeFromId(null, idString);
 JavaType javaTypeEnum = classResolver.typeFromId(null, idEnum);

 // Then
 assertEquals(Byte.TYPE, javaTypeByte.getRawClass());
 assertEquals(Integer.TYPE, javaTypeInt.getRawClass());
 assertEquals(Short.TYPE, javaTypeShort.getRawClass());
 assertEquals(Long.TYPE, javaTypeLong.getRawClass());
 assertEquals(Float.TYPE, javaTypeFloat.getRawClass());
 assertEquals(Double.TYPE, javaTypeDouble.getRawClass());
 assertEquals(Boolean.TYPE, javaTypeBoolean.getRawClass());
 assertEquals(Character.TYPE, javaTypeChar.getRawClass());
 assertEquals(String.class, javaTypeString.getRawClass());
 assertEquals(TestEnum.class, javaTypeEnum.getRawClass());
 }

 /**
 * Test for typeFromId method with Collection types.
 */
 @org.junit.Test
 public void testTypeFromIdCollectionTypes() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idArrayList = "java.util.ArrayList";
 String idEnumSet = "java.util.EnumSet";
 String idEnumMap = "java.util.EnumMap";

 // When
 JavaType javaTypeArrayList = classResolver.typeFromId(null, idArrayList);
 JavaType javaTypeEnumSet = classResolver.typeFromId(null, idEnumSet);
 JavaType javaTypeEnumMap = classResolver.typeFromId(null, idEnumMap);

 // Then
 assertEquals(ArrayList.class, javaTypeArrayList.getRawClass());
 assertEquals(EnumSet.class, javaTypeEnumSet.getRawClass());
 assertEquals(EnumMap.class, javaTypeEnumMap.getRawClass());
 }

 /**
 * Test for typeFromId method with MappedTypes.
 */
 @org.junit.Test
 public void testTypeFromIdMappedTypes() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idHashMap = "java.util.HashMap";
 String idHashtable = "java.util.Hashtable";
 String idLinkedHashMap = "java.util.LinkedHashMap";
 String idTreeMap = "java.util.TreeMap";

 // When
 JavaType javaTypeHashMap = classResolver.typeFromId(null, idHashMap);
 JavaType javaTypeHashtable = classResolver.typeFromId(null, idHashtable);
 JavaType javaTypeLinkedHashMap = classResolver.typeFromId(null, idLinkedHashMap);
 JavaType javaTypeTreeMap = classResolver.typeFromId(null, idTreeMap);

 // Then
 assertEquals(HashMap.class, javaTypeHashMap.getRawClass());
 assertEquals(Hashtable.class, javaTypeHashtable.getRawClass());
 assertEquals(LinkedHashMap.class, javaTypeLinkedHashMap.getRawClass());
 assertEquals(TreeMap.class, javaTypeTreeMap.getRawClass());
 }

 /**
 * Test for typeFromId method with Array types.
 */
 @org.junit.Test
 public void testTypeFromIdArrayTypes() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idByteArray = "byte[]";
 String idIntArray = "int[]";
 String idDoubleArray = "double[]";

 // When
 JavaType javaTypeByteArray = classResolver.typeFromId(null, idByteArray);
 JavaType javaTypeIntArray = classResolver.typeFromId(null, idIntArray);
 JavaType javaTypeDoubleArray = classResolver.typeFromId(null, idDoubleArray);

 // Then
 assertEquals(byte[].class, javaTypeByteArray.getRawClass());
 assertEquals(int[].class, javaTypeIntArray.getRawClass());
 assertEquals(double[].class, javaTypeDoubleArray.getRawClass());
 }

 /**
 * Test for typeFromId method with custom class.
 */
 @org.junit.Test
 public void testTypeFromIdCustomClass() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TestCustomClass customClass = new TestCustomClass();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idCustomClass = customClass.getClass().getName();

 // When
 JavaType javaTypeCustomClass = classResolver.typeFromId(null, idCustomClass);

 // Then
 assertEquals(TestCustomClass.class, javaTypeCustomClass.getRawClass());
 }

 /**
 * Test for typeFromId method with inner class.
 */
 @org.junit.Test
 public void testTypeFromIdInnerClass() throws IOException {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TestOuterClass outerClass = new TestOuterClass();
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 String idInnerClass = TestOuterClass.InnerClass.class.getName();

 // When
 JavaType javaTypeInnerClass = classResolver.typeFromId(null, idInnerClass);

 // Then
 assertEquals(TestOuterClass.InnerClass.class, javaTypeInnerClass.getRawClass());
 }

 /**
 * Test for getDescForKnownTypeIds method.
 */
 @org.junit.Test
 public void testGetDescForKnownTypeIds() {
 TypeIdResolver classResolver = new ClassNameIdResolver(Object.class.getTypeName(), null);
 assertEquals("class name used as type id", classResolver.getDescForKnownTypeIds());
 }
}

 enum TestEnum {
 TEST_VALUE
}

 class TestCustomClass {
 // Empty class for testing purposes
}

 class TestOuterClass {
 static class InnerClass {
 // Empty class for testing purposes
 }
}