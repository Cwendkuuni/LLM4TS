package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

 private TypeFactory typeFactory = new SimpleTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonical = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonical);
 assertNotNull(parsedType);
 assertEquals(JavaType.getType(String.class), parsedType);
 }

 @Test
 public void testParseArrayType() {
 String canonical = "[I";
 JavaType parsedType = typeParser.parse(canonical);
 assertNotNull(parsedType);
 assertEquals(JavaType.getType(int[].class), parsedType);
 }

 @Test
 public void testParseListType() {
 String canonical = "java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonical);
 assertNotNull(parsedType);
 JavaType elementType = parsedType.getContentType();
 assertEquals(JavaType.getType(String.class), elementType);
 assertEquals(TypeBindings.collections.list(), parsedType.getParameterTypes().get(0));
 }

 @Test
 public void testParseMapType() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType parsedType = typeParser.parse(canonical);
 assertNotNull(parsedType);
 JavaType keyType = parsedType.getParameterTypes().get(0);
 JavaType valueType = parsedType.getParameterTypes().get(1);
 assertEquals(JavaType.getType(String.class), keyType);
 assertEquals(JavaType.getType(Integer.class), valueType);
 }

 @Test
 public void testParseGenericTypeWithMultipleTypeParameters() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType parsedType = typeParser.parse(canonical);
 assertNotNull(parsedType);
 JavaType keyType = parsedType.getParameterTypes().get(0);
 JavaType valueType = parsedType.getParameterTypes().get(1);
 JavaType listElementType = valueType.getContentType();
 assertEquals(JavaType.getType(String.class), keyType);
 assertEquals(JavaType.getType(List.class), valueType);
 assertEquals(JavaType.getType(Integer.class), listElementType);
 }

 @Test
 public void testParseInvalidType() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer<java.lang.String>";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }

 @Test
 public void testParseInvalidToken1() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>ABC";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(canonical));
 }

 @Test
 public void testParseInvalidToken2() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 typeParser.parseType(tokens);
 tokens.pushBack(">");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens));
 }

 @Test
 public void testParseInvalidToken3() {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 typeParser.parseType(tokens);
 tokens.pushBack(",");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens));
 }

 private static class SimpleTypeFactory extends TypeFactory {
 @Override
 public JavaType findClass(String className) throws ClassNotFoundException {
 return super.findClass(className);
 }
 }

 final static class MyTokenizer extends org.junit.internal.TextRuntimeOutputReceiver.MyTokenizer {
 // empty
 }
}