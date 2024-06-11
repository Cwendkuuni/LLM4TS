package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTyping;

/**
 * Test class for {@link TypeParser}.
 */
public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory().defaultTyping(DefaultTyping.STATIC);
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() throws Exception {
 JavaType javaType = typeParser.parse("int");
 Class<?> clazz = javaType.getRawClass();
 assertEquals(int.class, clazz);
 }

 @Test
 public void testParseClassType() throws Exception {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>");
 TypeBindings bindings = javaType.getTypeBindings();
 assertEquals(java.util.List.class, bindings.getRawClass());
 assertEquals(java.lang.Integer.class, bindings.getTypeArguments().get(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 // invalid type
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer> java.lang.String>");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTypeWithComma() throws Exception {
 JavaType javaType1 = typeParser.parse("java.util.List<java.lang.Integer>, java.util.Map<java.lang.String, java.lang.String>");
 JavaType javaType2 = javaType1.getComponentType();
 assertEquals(java.lang.Integer.class, javaType2.getRawClass());
 JavaType javaTypeList = javaType1.getTypeArguments().get(0);
 JavaType javaTypeMap = javaType1.getTypeArguments().get(1);
 assertEquals(java.util.List.class, javaTypeList.getRawClass());
 assertEquals(java.util.Map.class, javaTypeMap.getRawClass());
 assertEquals(java.lang.String.class, javaTypeMap.getTypeArguments().get(0).getRawClass());
 assertEquals(java.lang.String.class, javaTypeMap.getTypeArguments().get(1).getRawClass());
 }

 @Test
 public void testParseTypeWithMultipleGenerics() throws Exception {
 JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>");
 JavaType javaTypeMap = javaType.getTypeArguments().get(0);
 JavaType javaTypeMapInner = javaTypeMap.getTypeArguments().get(0);
 assertEquals(java.util.Map.class, javaTypeMap.getRawClass());
 assertEquals(java.lang.String.class, javaTypeMap.getTypeArguments().get(0).getRawClass());
 assertEquals(java.util.Map.class, javaTypeMap.getTypeArguments().get(1).getRawClass());
 assertEquals(java.lang.String.class, javaTypeMapInner.getTypeArguments().get(0).getRawClass());
 assertEquals(java.lang.Integer.class, javaTypeMapInner.getTypeArguments().get(1).getRawClass());
 }

 @Test
 public void testParseTypeWithArray() throws Exception {
 JavaType javaType = typeParser.parse("java.lang.Integer[]");
 Class<?> clazz = javaType.getRawClass();
 assertEquals(int[].class, clazz.getComponentType());
 assertEquals(int.class, clazz.getComponentType().getComponentType());
 }

 @Test
 public void testParseTypeWithArrayAndGeneric() throws Exception {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer[]>");
 JavaType javaTypeList = javaType.getComponentType();
 assertEquals(java.util.List.class, javaTypeList.getRawClass());
 JavaType javaTypeArray = javaTypeList.getTypeArguments().get(0);
 assertEquals(int[].class, javaTypeArray.getRawClass());
 assertEquals(int.class, javaTypeArray.getRawClass().getComponentType().getComponentType());
 }
}