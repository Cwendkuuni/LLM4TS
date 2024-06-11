package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Test class for TypeParser.
 */
public class TypeParserTest {

 private static TypeFactory typeFactory = new TypeFactoryBuilder()
         .build();
 private static TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseEmptyType() throws Exception {
     JavaType javaType = typeParser.parse("void");
     assertNull(javaType.getRawClass());
     assertEquals(javaType.getRawClass().getName(), "void");
 }

 @Test
 public void testParseBasicType() throws Exception {
     JavaType javaType = typeParser.parse("int");
     assertEquals(javaType.getRawClass(), Integer.TYPE);
 }

 @Test
 public void testParseClassType() throws Exception {
     JavaType javaType = typeParser.parse("java.lang.String");
     assertEquals(javaType.getRawClass(), String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
     JavaType javaType = typeParser.parse("invalidType");
     fail("Expected IllegalArgumentException but got: " + javaType);
 }

 @Test
 public void testParseArrayType() throws Exception {
     JavaType javaType = typeParser.parse("[Ljava.lang.Object;");
     assertEquals(javaType.getRawClass(), Object[].class);
 }

 @Test
 public void testParseGenericType() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
     JavaType elementType = javaType.getContentType();
     assertEquals(elementType.getRawClass(), String.class);
     assertEquals(javaType.getRawClass(), List.class);
     assertEquals(javaType.getGenericTypes(), Arrays.asList(String.class, List.class));
 }

 @Test
 public void testParseGenericTypeWithUpperBounds() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<java.util.Collections.SingletonList<java.lang.String>>");
     JavaType elementType = javaType.getContentType();
     assertEquals(elementType.getRawClass(), SingletonList.class);
     assertEquals(SingletonList.class, javaType.getRawClass());
     assertEquals(javaType.getGenericTypes(), Arrays.asList(String.class, SingletonList.class, List.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidGenericType() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<java.lang.String> <java.lang.Integer>");
     fail("Expected IllegalArgumentException but got: " + javaType);
 }

 @Test
 public void testParseTypeWithTypeVariable() throws Exception {
     JavaType javaType = typeParser.parse("java.util.Map<String,java.util.List<Integer>>");
     JavaType mapType = javaType.getRawClass();
     assertEquals(mapType.getRawClass(), Map.class);
     JavaType stringKeyType = javaType.getParameterTypes()[0];
     JavaType listType = javaType.getParameterTypes()[1];
     assertEquals(stringKeyType.getRawClass(), String.class);
     assertEquals(listType.getRawClass(), List.class);
     assertEquals(listType.getContentType().getRawClass(), Integer.class);
 }

 @Test
 public void testParseTypeWithTypeVariableAndBounds() throws Exception {
     JavaType javaType = typeParser.parse("java.util.Map<String,java.util.List<? extends Number>>");
     JavaType mapType = javaType.getRawClass();
     assertEquals(mapType.getRawClass(), Map.class);
     JavaType stringKeyType = javaType.getParameterTypes()[0];
     JavaType listType = javaType.getParameterTypes()[1];
     assertEquals(stringKeyType.getRawClass(), String.class);
     assertEquals(listType.getRawClass(), List.class);
     assertEquals(listType.getRawClass(), List.class);
     assertEquals(listType.getRawType().getComponentType().getRawClass(), Number.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeVariableAndBounds() throws Exception {
     JavaType javaType = typeParser.parse("java.util.Map<String,java.util.List<? extends Number,Integer>>");
     fail("Expected IllegalArgumentException but got: " + javaType);
 }

 @Test
 public void testParseTypeWithWildcard() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<? extends Number>");
     JavaType listType = javaType.getRawClass();
     assertEquals(listType.getRawClass(), List.class);
     assertEquals(listType.getRawType().getComponentType().getRawClass(), Number.class);
 }

 @Test
 public void testParseTypeWithParameters() throws Exception {
     JavaType javaType = typeParser.parse("java.lang.String.<init>(java.lang.String, int, java.lang.String)");
     JavaType constructorType = javaType.getRawClass();
     assertEquals(constructorType.getRawClass(), String.class);
     List<JavaType> parameterTypes = javaType.getGenericParameterTypes();
     assertEquals(parameterTypes.size(), 3);
     assertEquals(parameterTypes.get(0).getRawClass(), String.class);
     assertEquals(parameterTypes.get(1).getRawClass(), int.class);
     assertEquals(parameterTypes.get(2).getRawClass(), String.class);
 }

 @Test
 public void testParseTypeWithNestedGenerics() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<java.util.Map<String,java.util.List<Integer>>>");
     JavaType listType = javaType.getRawClass();
     assertEquals(listType.getRawClass(), List.class);
     JavaType mapType = listType.getContentType();
     assertEquals(mapType.getRawClass(), Map.class);
     JavaType mapKeyType = mapType.getParameterTypes()[0];
     JavaType mapValueType = mapType.getParameterTypes()[1];
     JavaType stringKeyType = mapKeyType.getRawClass();
     JavaType listValueType = mapValueType.getRawClass();
     assertEquals(stringKeyType.getRawClass(), String.class);
     assertEquals(listValueType.getRawClass(), List.class);
     JavaType listElementType = listValueType.getContentType();
     assertEquals(listElementType.getRawClass(), Integer.class);
 }

 @Test
 public void testParseTypeWithAnonymousInnerClass() throws Exception {
     JavaType javaType = typeParser.parse("new class <T>{}<java.lang.String, java.util.ArrayList<java.lang.Byte>>");
     JavaType anonymousInnerClassType = javaType.getRawClass();
     assertEquals(anonymousInnerClassType.getRawClass(), anonymousInnerClassType.getComponentType().getComponentType().getRawClass());
     JavaType outerType = javaType.getAnnotatedInnerClassTypes()[0];
     assertEquals(outerType.getAnnotations().length, 0);
     assertEquals(outerType.getRawClass(), Object.class);
     JavaType innerType = outerType.getAnnotatedInnerClassTypes()[0];
     assertEquals(innerType.getAnnotations().length, 0);
     assertEquals(innerType.getRawClass(), anonymousInnerClassType.getComponentType().getComponentType().getRawClass());
     JavaType genericType1 = innerType.getGenericParameterTypes()[0];
     JavaType genericType2 = innerType.getGenericParameterTypes()[1];
     assertEquals(genericType1.getRawClass(), String.class);
     assertEquals(genericType2.getRawClass(), ArrayList.class);
     JavaType elementType = genericType2.getContentType();
     assertEquals(elementType.getRawClass(), Byte.class);
 }

 @Test
 public void testParseTypeWithAnonymousOuterClass() throws Exception {
     JavaType javaType = typeParser.parse("new class <T> { public static class InnerClass<S> {}}.<T>");
     JavaType outerClassType = javaType.getRawClass();
     JavaType innerClassType = outerClassType.getAnnotatedInnerClassTypes()[0];
     assertEquals(outerClassType.getRawClass(), Object.class);
     assertEquals(innerClassType.getRawClass(), Object.class);
     assertEquals(innerClassType.getTypeBindings().getRawClass(), Object.class);
     assertEquals(innerClassType.getAnnotations().length, 0);
 }

 /**
  * Test utility method for printing list of types.
  */
 private void printTypes(List<JavaType> types) {
     System.out.print("Types: [");
     for (int i = 0; i < types.size(); i++) {
         JavaType javaType = types.get(i);
         System.out.print(javaType.getRawClass().getName());
         if (i != types.size() - 1) {
             System.out.print(", ");
         }
     }
     System.out.println("]");
 }

 @Test
 public void testParseTypeWithComplexGenericTypes() throws Exception {
     JavaType javaType = typeParser.parse("java.util.List<java.util.Optional<java.util.List<java.util.List<java.util.Optional<java.lang.String>>>>>");
     printTypes(Arrays.asList(javaType.getAnnotatedInnerClassTypes()));
     printTypes(javaType.getGenericParameterTypes());
 }
}