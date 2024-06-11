package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(String.class, javaType.getRawClass());
 assertEquals(Integer.TYPE, javaType.getType());
 }

 @Test
 public void testParseClassNameWithWildcard() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(Integer.TYPE, typeBindings.getTypeVar("T").getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "int<java.lang.String>";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseEmptyListType() {
 String canonical = "java.util.List<>";
 JavaType javaType = typeParser.parse(canonical);
 assertNull(javaType.getTypeBindings());
 }

 @Test
 public void testParseListTypeWithMultipleTypes() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 assertEquals(2, typeParameters.size());
 assertEquals(Integer.TYPE, typeParameters.get(0).getType());
 assertEquals(String.class, typeParameters.get(1).getType());
 }

 @Test
 public void testParseNestedClasses() {
 String canonical = "com.example.test.NestedClass$InnerClass";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals("InnerClass", javaType.getRawClass().getSimpleName());
 }

 @Test
 public void testParseListWithNestedClasses() {
 String canonical = "java.util.List<com.example.test.NestedClass$InnerClass>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 assertEquals(1, typeParameters.size());
 JavaType innerClassType = typeParameters.get(0);
 assertEquals("InnerClass", innerClassType.getRawClass().getSimpleName());
 }

 @Test
 public void testParseArrayType() {
 String canonical = "int[]";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(int[].class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayTypeWithGenericType() {
 String canonical = "java.util.List<java.lang.Integer>[]";
 JavaType javaType = typeParser.parse(canonical);
 JavaType elementType = javaType.getTypeBindings().getTypeVar("T").getType();
 assertEquals(JavaType.constructParametricType(List.class, Integer.TYPE), elementType);
 }

 @Test
 public void testParseListTypeWithArrayElement() {
 String canonical = "java.util.List<int[]>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 JavaType arrayElementType = typeParameters.get(0).getType();
 assertEquals(int[].class, arrayElementType.getRawClass());
 }

 @Test
 public void testParseListTypeWithArrayElementsAndGenerics() {
 String canonical = "java.util.List<int[]>[, java.lang.List<java.lang.String>]";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 assertEquals(2, typeParameters.size());
 JavaType arrayElementType = typeParameters.get(0).getType();
 assertEquals(int[].class, arrayElementType.getRawClass());
 JavaType listType = typeParameters.get(1).getType();
 JavaType listElementType = listType.getTypeBindings().getTypeVar("T").getType();
 assertEquals(String.class, listElementType.getRawClass());
 }

 @Test
 public void testParseMapType() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 assertEquals(2, typeParameters.size());
 assertEquals(String.class, typeParameters.get(0).getType());
 assertEquals(Integer.TYPE, typeParameters.get(1).getType().getType());
 }

 @Test
 public void testParseMapTypeWithGenericValues() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeParameters = javaType.getTypeBindings().getTypeVars();
 assertEquals(2, typeParameters.size());
 assertEquals(String.class, typeParameters.get(0).getType());
 JavaType valueType = typeParameters.get(1).getType();
 List<JavaType> valueTypeParameters = valueType.getTypeBindings().getTypeVars();
 assertEquals(1, valueTypeParameters.size());
 assertEquals(Integer.TYPE, valueTypeParameters.get(0).getType().getType());
 }
}