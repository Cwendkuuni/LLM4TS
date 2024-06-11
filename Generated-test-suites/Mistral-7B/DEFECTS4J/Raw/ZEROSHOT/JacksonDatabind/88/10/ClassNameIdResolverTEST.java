package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final ClassNameIdResolver resolveInstance;

 static {
 try {
 resolveInstance = new ClassNameIdResolver(String.class.getDeclaredType(JsonTypeInfo.Id.CLASS), new TypeFactory());
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testIdFromValue() {
 Object obj = new Object();
 String expectedId = resolveInstance.idFromValue(obj);
 String actualId = resolveInstance.idFromValue(obj, Object.class);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testIdFromValueAndType() {
 Object obj = new Object();
 String expectedId = resolveInstance.idFromValueAndType(obj, String.class);
 String actualId = resolveInstance.idFromValueAndType(obj, Object.class);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testTypeFromId() throws IOException {
 DatabindContext context = new ObjectMapper().getFactory().createContext();
 TypeFactory tf = context.getTypeFactory();
 String id = "java.lang.String";
 JavaType expectedType = tf.constructType(id);
 JavaType actualType = resolveInstance.typeFromId(context, id);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws IOException {
 DatabindContext context = new ObjectMapper().getFactory().createContext();
 String invalidId = "InvalidTypeId";
 resolveInstance.typeFromId(context, invalidId);
 }

 @Test
 public void testIdFromValueWithEnum() {
 Enum e = Enum.valueOf(Integer.class, "ONE");
 String expectedId = resolveInstance.idFromValue(e);
 String actualId = resolveInstance.idFromValue(e, Integer.class);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testIdFromValueWithEnumSuperClass() {
 Enum superEnum = Enum.valueOf(Enum.class, "Object");
 String expectedId = resolveInstance.idFromValue(superEnum);
 String actualId = resolveInstance.idFromValue(superEnum, Enum.class);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testTypeFromIdWithGenericCollection() throws IOException {
 DatabindContext context = new ObjectMapper().getFactory().createContext();
 TypeFactory tf = context.getTypeFactory();
 TypeType listType = tf.constructType(List.class);
 TypeType stringType = tf.constructType(String.class);
 TypeType listOfStringType = tf.constructType(listType.wrap(stringType));
 String listId = listOfStringType.toString();
 JavaType expectedType = tf.constructType(listId);
 JavaType actualType = resolveInstance.typeFromId(context, listId);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testTypeFromIdWithEnumMap() throws IOException {
 DatabindContext context = new ObjectMapper().getFactory().createContext();
 TypeFactory tf = context.getTypeFactory();
 TypeType stringType = tf.constructType(String.class);
 TypeType enumType = tf.constructType(Enum.class);
 TypeType enumMapType = tf.constructType(Map.class.getTypeParameters()[0].getType(), enumType);
 String enumMapId = enumMapType.toString();
 JavaType expectedType = tf.constructType(enumMapId);
 JavaType actualType = resolveInstance.typeFromId(context, enumMapId);
 assertEquals(expectedType, actualType);
 }
}