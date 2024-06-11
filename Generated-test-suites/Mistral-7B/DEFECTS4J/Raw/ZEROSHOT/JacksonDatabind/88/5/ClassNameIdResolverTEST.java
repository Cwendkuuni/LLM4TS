package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final TypeFactory typeFactory = new TypeFactoryBuilder().build();
 private static final JavaType baseType = typeFactory.constructType(String.class);
 private static final ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

 @Test
 public void testIdFromValue() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValue(obj);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object obj = new Object();
 Class<?> type = Integer.class;
 String id = resolver.idFromValueAndType(obj, type);
 fail("Expected exception: not supported");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromValueAndTypeInvalid() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValueAndType(obj, "invalidId");
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType javaType = resolver.typeFromId(null, id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdIOException() throws Exception {
 String id = "InvalidTypeId";
 JavaType javaType = resolver.typeFromId(null, id);
 fail("Expected IOException");
 }

 @Test
 public void testTypeFromIdUnknownClass() throws Exception {
 String id = "com.non.existent.package.NonExistentClass";
 JavaType javaType = resolver.typeFromId(null, id);
 assertNull(javaType);
 }

 @Test
 public void testTypeFromIdGenericType() throws Exception {
 TypeFactory tf = new TypeFactoryBuilder().build();
 JavaType javaType = tf.constructType(List.class, Integer.class);
 String id = javaType.toCanonical();
 JavaType resolvedType = resolver.typeFromId(null, id);
 assertEquals(javaType, resolvedType);
 }

 @Test
 public void testIdFromValueEnum() throws Exception {
 Enum e = Enum.valueOf(MyEnum.class, "ONE");
 String id = resolver.idFromValue(e);
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.MyEnum", id);
 }

 @Test
 public void testIdFromValueInnerClass() throws Exception {
 Class<?> outer = MyOuterClass.class;
 String id = resolver.idFromValue(new MyOuterClass.MyInnerClass());
 String expected = outer.getName() + "$" + MyOuterClass.MyInnerClass.class.getSimpleName();
 assertEquals(expected, id);
 }

 @Test
 public void testTypeFromIdInnerClass() throws Exception {
 String id = MyOuterClass.MyInnerClass.class.getName();
 JavaType javaType = resolver.typeFromId(null, id);
 assertEquals(MyOuterClass.MyInnerClass.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueArray() throws Exception {
 int[] arr = {1, 2, 3};
 String id = resolver.idFromValue(arr);
 assertEquals("java.lang.int[]", id);
 }
}

class MyEnum {
 @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
 public enum MyEnum {
 ONE, TWO, THREE
 }
}

class MyOuterClass {
 @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
 public static class MyInnerClass {

 }
}