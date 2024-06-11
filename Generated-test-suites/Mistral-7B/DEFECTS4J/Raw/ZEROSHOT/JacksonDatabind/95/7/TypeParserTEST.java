package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Test
 public void testTypeParserConstruction() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleClass() {
 JavaType javaType = typeParser.parse("int");
 assertNotNull(javaType);
 assertEquals(Integer.TYPE, javaType.getRawClass());
 }

 @Test
 public void testParseList() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>");
 assertNotNull(javaType);
 List<JavaType> componentTypes = javaType.getGenerics();
 assertEquals(1, componentTypes.size());
 JavaType componentType = componentTypes.get(0);
 assertEquals(Integer.TYPE, componentType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>abcd");
 }

 @Test
 public void testParseTypesWithCommas() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>, java.util.List<java.lang.String>");
 assertNotNull(javaType);
 List<JavaType> componentTypes = javaType.getGenerics();
 assertEquals(2, componentTypes.size());
 JavaType intType = componentTypes.get(0);
 assertEquals(Integer.TYPE, intType.getRawClass());
 JavaType stringType = componentTypes.get(1);
 assertEquals(String.class, stringType.getRawClass());
 }

 @Test
 public void testParseEmptyList() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer> {}", TypeFactory.defaultInstance());
 assertNotNull(javaType);
 List<JavaType> componentTypes = javaType.getGenerics();
 assertEquals(1, componentTypes.size());
 JavaType componentType = componentTypes.get(0);
 assertEquals(Integer.TYPE, componentType.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() {
 JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
 assertNotNull(javaType);
 JavaType keyType = javaType.getGeneric(0);
 assertEquals(String.class, keyType.getRawClass());
 JavaType valueType = javaType.getGeneric(1);
 List<JavaType> valueTypes = valueType.getGenerics();
 assertEquals(1, valueTypes.size());
 JavaType intType = valueTypes.get(0);
 assertEquals(Integer.TYPE, intType.getRawClass());
 }

 @Test
 public void testParseClassWithMultipleGenerics() {
 JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> {}", TypeFactory.defaultInstance());
 assertNotNull(javaType);
 JavaType keyType = javaType.getGeneric(0);
 assertEquals(String.class, keyType.getRawClass());
 JavaType valueType = javaType.getGeneric(1);
 List<JavaType> valueTypes = valueType.getGenerics();
 assertEquals(1, valueTypes.size());
 JavaType intType = valueTypes.get(0);
 assertEquals(Integer.TYPE, intType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidGenerics() {
 JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>abcd> {}", TypeFactory.defaultInstance());
 }

 @Test
 public void testPushBackToken() {
 String input = "java.util.List<java.lang.Integer>, java.util.List<java.lang.String>";
 MyTokenizer tokenizer = new MyTokenizer(input);
 typeParser = new TypeParser(typeFactory);
 typeParser.withFactory(typeFactory);
 typeParser.parseType(tokenizer);
 List<JavaType> types = typeParser.parseTypes(tokenizer);
 tokenizer.pushBack(",");
 assertEquals(2, types.size());
 }

 @Test
 public void testParseEmptyType() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer> {}", TypeFactory.defaultInstance());
 assertNotNull(javaType);
 List<JavaType> componentTypes = javaType.getGenerics();
 assertEquals(0, componentTypes.size());
 }
}