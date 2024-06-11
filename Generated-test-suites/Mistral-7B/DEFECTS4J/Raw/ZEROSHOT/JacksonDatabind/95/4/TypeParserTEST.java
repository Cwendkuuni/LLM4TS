package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory = new SimpleTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalType = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertEquals(String.class, parsedType.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 String canonicalType = "[I";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertArrayType(parsedType, int[].class);
 }

 @Test
 public void testParseListType() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType parsedType = typeParser.parse(canonicalType);
 ListType listType = (ListType) parsedType;
 Class<?> elementType = listType.getContentType();
 assertEquals(Integer.class, elementType);
 }

 @Test
 public void testParseMapType() {
 String canonicalType = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType parsedType = typeParser.parse(canonicalType);
 MapType mapType = (MapType) parsedType;
 Class<?> keyType = mapType.getKeyType();
 Class<?> valueType = mapType.getContentType();
 assertEquals(String.class, keyType);
 assertEquals(Integer.class, valueType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.lang.String<java.util.List";
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException for invalid type: " + canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndToken() {
 String canonicalType = "java.lang.String<java.util.List";
 MyTokenizer tokens = new MyTokenizer(canonicalType.trim());
 typeParser.parseType(tokens);
 fail("Expected IllegalArgumentException for missing end token: " + tokens.getAllInput());
 }

 @Test
 public void testParseTypeWithGenerics() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonicalType);
 ListType listType = (ListType) parsedType;
 Class<?> elementType = listType.getContentType();
 assertEquals(String.class, elementType);
 }

 @Test
 public void testParseTypeWithMultipleGenerics() {
 String canonicalType = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType parsedType = typeParser.parse(canonicalType);
 MapType mapType = (MapType) parsedType;
 Class<?> keyType = mapType.getKeyType();
 ListType listType = (ListType) mapType.getContentType();
 Class<?> elementType = listType.getContentType();
 assertEquals(String.class, keyType);
 assertEquals(Integer.class, elementType);
 }

 @Test
 public void testParseTypeWithDeepGenerics() {
 String canonicalType = "java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>>";
 JavaType parsedType = typeParser.parse(canonicalType);
 ListType listType = (ListType) parsedType;
 MapType mapType = (MapType) listType.getContentType();
 Class<?> keyType = mapType.getKeyType();
 ListType listTypeInner = (ListType) mapType.getContentType();
 Class<?> elementTypeInner = listTypeInner.getContentType();
 assertEquals(String.class, keyType);
 assertEquals(Integer.class, elementTypeInner);
 }

 private void assertArrayType(JavaType parsedType, Class<?> expectedType) {
 ArrayType arrayType = (ArrayType) parsedType;
 assertEquals(expectedType.getComponentType(), arrayType.getContentType());
 }
}