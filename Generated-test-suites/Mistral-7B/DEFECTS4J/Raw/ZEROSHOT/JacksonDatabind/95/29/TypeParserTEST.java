package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() {
 JavaType javaType = typeParser.parse("int");
 JavaType expectedJavaType = typeFactory.constructType(int.class);
 assertEquals(expectedJavaType, javaType);
 }

 @Test
 public void testParseClassType() {
 JavaType javaType = typeParser.parse("java.lang.String");
 JavaType expectedJavaType = typeFactory.constructType(String.class);
 assertEquals(expectedJavaType, javaType);
 }

 @Test
 public void testParseArrayType() {
 JavaType javaType = typeParser.parse("int[]");
 JavaType expectedJavaType = typeFactory.constructType(int[].class);
 assertEquals(expectedJavaType, javaType);
 }

 @Test
 public void testParseGenericType() {
 JavaType javaType = typeParser.parse("java.util.ArrayList<java.lang.String>");
 JavaType expectedJavaType = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());
 assertEquals(expectedJavaType, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnexpectedToken() {
 String input = "int<";
 try {
 typeParser.parse(input);
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type '"+input+"' (remaining: '>', expected ')' or '>')", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnexpectedEndOfString() {
 String input = "int";
 try {
 typeParser.parse(input + "x");
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'intx' (remaining: 'x', expected end-of-string)", e.getMessage());
 }
 }

 @Test
 public void testParseWithInvalidClassName() {
 String input = "java.lang.XString";
 try {
 typeParser.parse(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Can not locate class 'XString', problem: ", e.getMessage());
 }
 }

 @Test
 public void testParseTypesWithEmptyList() {
 JavaType javaType = typeParser.parse("List<>");
 JavaType expectedJavaType = typeFactory.constructType(new TypeReference<List<?>>(){}.getType());
 assertEquals(expectedJavaType, javaType);
 }

 @Test
 public void testParseTypesWithSingleType() {
 JavaType javaType = typeParser.parse("List<int>");
 JavaType expectedJavaType = typeFactory.constructType(new TypeReference<List<Integer>>(){}.getType());
 assertEquals(expectedJavaType, javaType);
 }

 @Test
 public void testParseTypesWithMultipleTypes() {
 JavaType javaType = typeParser.parse("List<int, String>");
 JavaType expectedJavaType = typeFactory.constructType(new TypeReference<List<Integer>>(){}.getType());
 JavaType arg2Type = typeFactory.constructType(String.class);
 List<JavaType> expectedTypes = Arrays.asList(expectedJavaType, arg2Type);
 JavaType javaTypesType = typeFactory.constructType(new TypeReference<List<JavaType>>(){}.getType());
 JavaType expectedJavaTypesType = typeFactory.constructType(javaTypesType.getType());
 assertEquals(expectedJavaTypesType, javaTypesType.getType());
 assertEquals(expectedTypes, javaType.getTypeArgs());
 }

 @Test
 public void testParseTypesWithInvalidToken() {
 String input = "List<int, String, >";
 try {
 typeParser.parse(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'List<int, String, >' (remaining: '>', expected ',' or '>')", e.getMessage());
 }
 }
}