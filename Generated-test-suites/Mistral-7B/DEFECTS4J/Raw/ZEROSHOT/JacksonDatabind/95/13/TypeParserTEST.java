package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;
import com.fasterxml.jackson.databind.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(JavaType.getTypeFactory().constructType(int.class), javaType);
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.util.List";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(typeFactory.constructCollectionType(List.class, Object.class), javaType);
 }

 @Test
 public void testParseClassTypeWithGenericType() {
 String canonical = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(typeFactory.constructCollectionType(List.class, String.class), javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.util.List<java.lang.Integer>abc";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseInvalidType2() {
 String canonical = "java.util.List<java.lang.Integer> < >";
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseInvalidType3() {
 String canonical = "java.util.List<java.lang.Integer> < java.lang.String >";
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseCustomClassType() {
 String canonical = "com.example.MyCustomClass";
 JavaType javaType = typeParser.parse(canonical);
 // Assuming MyCustomClass is a valid java class
 assertEquals(typeFactory.constructClass(MyCustomClass.class), javaType);
 }

 @Test
 public void testParseCustomClassTypeWithGenericType() {
 String canonical = "com.example.MyCustomClass<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 // Assuming MyCustomClass is a valid java class with a generic type
 Type type = MyCustomClass.class.getGenericSuperclass();
 Type[] typesArgs = ((ParameterizedType) type).getActualTypeArguments();
 assertEquals(typesArgs[0].getTypeName().toString(), String.class.getName());
 assertEquals(typeFactory.constructType(MyCustomClass.class, String.class), javaType);
 }

 @Test
 public void testParseCustomClassTypeWithGenericTypes() {
 String canonical = "com.example.MyCustomClass<java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>>";
 JavaType javaType = typeParser.parse(canonical);
 // Assuming MyCustomClass is a valid java class with multiple generic types
 Type type = MyCustomClass.class.getGenericSuperclass();
 Type[] typesArgs = ((ParameterizedType) type).getActualTypeArguments();
 assertEquals(typesArgs[0].getTypeName().toString(),
 List.class.getName() + "<" + String.class.getName() + ">");
 assertEquals(typesArgs[1].getTypeName().toString(),
 Map.class.getName() + "<" + String.class.getName() + ", " + Integer.class.getName() + ">");
 assertEquals(typeFactory.constructType(MyCustomClass.class,
 List.class, Map.class), javaType);
 }

 @Test
 public void testParseInvalidToken() {
 String canonical = "int<abc";
 try {
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'int<abc' (remaining: 'bc'): " +
 "Unexpected token 'b' (char: 98), expected ',' or '>')";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseUnexpectedEndOfString() {
 String canonical = "int<";
 try {
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'int<' (remaining: ')'): " +
 "Unexpected end-of-string";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

}