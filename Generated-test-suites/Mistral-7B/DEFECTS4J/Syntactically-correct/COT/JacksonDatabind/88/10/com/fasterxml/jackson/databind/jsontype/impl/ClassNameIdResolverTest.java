package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private TypeFactory typeFactory;
 private DatabindContext context;

 @Before
 public void setup() throws Exception {
 resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 context = new TestObjectMapper().getDatabindContext();
 }

 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 // Not applicable to ClassNameIdResolver
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = resolver.idFromValue(value);
 assertEquals(value.getClass().getName(), id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> cls = String.class;
 String id = resolver.idFromValueAndType(value, cls);
 assertEquals(cls.getName(), id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType javaType = resolver.typeFromId(context, id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromValueNullValue() {
 resolver.idFromValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdNullId() throws IOException {
 String id = null;
 resolver.typeFromId(context, id);
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdIOException() throws IOException {
 Class<?> type = Object.class;
 String id = "com.nonexistent.class.name";
 JavaType javaType = resolver.typeFromId(context, id);
 fail("Should have thrown IOException");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testTypeFromIdClassNotFoundException() throws IOException {
 Class<?> type = Object.class;
 String id = "com.nonexistent.class.name$InnerClass";
 JavaType javaType = resolver.typeFromId(context, id);
 fail("Should have thrown ClassNotFoundException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdInvalidTypeId() throws IOException {
 Class<?> type = Object.class;
 String id = "invalidTypeId";
 JavaType javaType = resolver.typeFromId(context, id);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testIdFrom() {
 Object value = new Object();
 Class<?> cls = Integer.class;
 String id = resolver._idFrom(value, cls, typeFactory);
 assertEquals(cls.getName(), id);
 }

 @Test
 public void testIdFromEmptyClass() {
 Object value = new Object();
 Class<?> cls = Object.class;
 String id = resolver._idFrom(value, cls, typeFactory);
 assertEquals("", id);
 }

 @Test
 public void testIdFromEnum() {
 Enum<?> enumValue = Enum.valueOf(String.class, "FOO");
 Class<?> enumClass = enumValue.getClass();
 String id = resolver._idFrom(enumValue, enumClass, typeFactory);
 assertEquals(enumClass.getName(), id);
 }

 @Test
 public void testIdFromCollectionEnum() throws Exception {
 Enum<?> enumValue = Enum.valueOf(String.class, "FOO");
 EnumSet enumSet = EnumSet.of(enumValue);
 String id = resolver._idFrom(enumSet, EnumSet.class, typeFactory);
 assertEquals(EnumSet.class.getName() + "<" + String.class.getName() + ">", id);
 }

 @Test
 public void testIdFromMapEnum() throws Exception {
 Enum<?> enumValue = Enum.valueOf(String.class, "FOO");
 EnumMap enumMap = new EnumMap<>(String.class) {{ put(enumValue, "value"); }};
 String id = resolver._idFrom(enumMap, EnumMap.class, typeFactory);
 assertEquals(EnumMap.class.getName() + "<java.lang.String,java.lang.Object>", id);
 }

 @Test
 public void testIdFromArrays() {
 Object[] values = new Object[] { new Object() };
 String id = resolver._idFrom(values, Object[].class, typeFactory);
 assertEquals("java.lang.Object[]", id);
 }

 @Test
 public void testIdFromInnerClass() {
 Class<?> outerClass = Object.class;
 Class<?> innerClass = Object.class.getDeclaredClass("InnerClass");
 innerClass.setAccessible(true);
 Object value = new Object();
 String id = resolver._idFrom(value, innerClass, typeFactory);
 String expected = outerClass.getName() + "$" + innerClass.getSimpleName();
 assertEquals(expected, id);
 }

 @Test
 public void testIdFromArrayInnerClass() {
 Class<?> outerClass = Object.class;
 Class<?> innerClass = Object.class.getDeclaredClass("InnerClass$Array");
 innerClass.setAccessible(true);
 Object[] values = new Object[] { new Object() };
 String id = resolver._idFrom(values, innerClass.getComponentType().getCanonicalName().replaceAll("$", "").replaceAll("\\$Array", "").getClass(), typeFactory);
 String expected = outerClass.getName() + "$" + innerClass.getSimpleName() + "[]";
 assertEquals(expected, id);
 }
}