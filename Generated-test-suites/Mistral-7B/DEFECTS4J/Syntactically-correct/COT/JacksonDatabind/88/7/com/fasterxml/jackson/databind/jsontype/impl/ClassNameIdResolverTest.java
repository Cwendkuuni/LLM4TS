package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private TypeIdResolver _resolver;
 private TypeFactory _typeFactory;
 private JavaType _baseType;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 _typeFactory = new TypeFactoryBuilder()
 .baseInstance(Object.class)
 .build();
 _baseType = _typeFactory.constructType(Object.class);
 _resolver = new ClassNameIdResolver(_baseType, _typeFactory);
 }

 @Test
 public void testGetMechanism() {
 assertEquals(Id.CLASS, _resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 try {
 _resolver.registerSubtype(String.class, "String");
 fail("Should not reach here as registerSubtype not used with class name - based resolver");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = _resolver.idFromValue(value);
 assertEquals(value.getClass().getName(), id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String.class;
 String id = _resolver.idFromValueAndType(value, type);
 assertEquals(value.getClass().getName(), id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 DatabindContext context = new ObjectMapper().getDeserializationContext();
 String id = "java.lang.String";
 JavaType javaType = _resolver.typeFromId(context, id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdWithUnknownId() throws Exception {
 DatabindContext context = new ObjectMapper().getDeserializationContext();
 String id = "com.unknown.Class";
 JavaType javaType = _resolver.typeFromId(context, id);
 fail("Should have thrown IOException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdWithInvalidId() throws Exception {
 DatabindContext context = new ObjectMapper().getDeserializationContext();
 String id = "InvalidTypeId";
 JavaType javaType = _resolver.typeFromId(context, id);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testIdFrom() throws Exception {
 Object value = new Enum("TestEnumValue");
 Class<?> enumClass = value.getClass();
 ClassNameIdResolver resolver = new ClassNameIdResolver(_baseType, _typeFactory);
 String id = resolver._idFrom(value, enumClass, _typeFactory);

 Class<?> resultClass = _typeFactory.constructClass(id);
 assertEquals(enumClass, resultClass);
 }

 @Test
 public void testIdFromForJavaUtilCollections() throws Exception {
 Object value = Arrays.asList("Test1", "Test2");
 _resolver = new ClassNameIdResolver(_baseType, _typeFactory);
 String id = _resolver._idFrom(value, value.getClass(), _typeFactory);

 assertEquals("java.util.ArrayList", id);
 }

 @Test
 public void testIdFromForJavaUtilEnumSet() throws Exception {
 Object value = EnumSet.allOf(TestEnum.class);
 _resolver = new ClassNameIdResolver(_baseType, _typeFactory);
 String id = _resolver._idFrom(value, value.getClass(), _typeFactory);

 TypeReference<EnumSet<TestEnum>> typeRef = new TypeReference<EnumSet<TestEnum>>() {};
 Class<?> resultClass = _typeFactory.constructType(id, typeRef);
 assertEquals(EnumSet.class, resultClass.getComponentType());
 }

 @Test
 public void testIdFromForJavaUtilEnumMap() throws Exception {
 Object value = new EnumMap<>(TestEnum.class) {{ put(TestEnum.TEST1, "Test1Value"); }} ;
 _resolver = new ClassNameIdResolver(_baseType, _typeFactory);
 String id = _resolver._idFrom(value, value.getClass(), _typeFactory);

 TypeReference<Map<TestEnum, String>> typeRef = new TypeReference<Map<TestEnum, String>>() {};
 Class<?> resultClass = _typeFactory.constructType(id, typeRef);
 assertEquals(EnumMap.class, resultClass);
 }
}