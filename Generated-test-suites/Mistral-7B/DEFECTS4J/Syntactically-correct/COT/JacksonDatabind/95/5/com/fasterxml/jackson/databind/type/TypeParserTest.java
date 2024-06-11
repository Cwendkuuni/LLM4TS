package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory factory = new TypeFactory();
 private TypeParser parser;

 @Test
 public void testTypeParserWithFactory() throws Exception {
 parser = new TypeParser(factory);
 }

 @Test
 public void testParseWithValidCanonicalType() throws Exception {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType type = parser.parse(canonicalType);
 assertEquals(List.class, type.getRawClass());
 JavaType genericType = type.getGenericType();
 assertEquals(List.class, genericType.getRawClass());
 assertEquals(Integer.class, genericType.getGeneric(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidCanonicalType() throws Exception {
 String canonicalType = "java.util.List<java.lang.Integer>abc";
 parser.parse(canonicalType);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyCanonicalType() throws Exception {
 String canonicalType = "";
 parser.parse(canonicalType);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseType() throws Exception {
 parser = new TypeParser(factory);
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>");
 JavaType type = parser.parseType(tokens);
 assertEquals(List.class, type.getRawClass());
 JavaType genericType = type.getGenericType();
 assertEquals(List.class, genericType.getRawClass());
 assertEquals(Integer.class, genericType.getGeneric(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeWithUnexpectedToken() throws Exception {
 parser = new TypeParser(factory);
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>ab");
 parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTypes() throws Exception {
 parser = new TypeParser(factory);
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>,java.util.List<java.lang.String>");
 List<JavaType> types = parser.parseTypes(tokens);
 assertEquals(2, types.size());
 JavaType firstType = types.get(0);
 assertEquals(List.class, firstType.getRawClass());
 JavaType firstGenericType = firstType.getGenericType();
 assertEquals(List.class, firstGenericType.getRawClass());
 assertEquals(Integer.class, firstGenericType.getGeneric(0).getRawClass());
 JavaType secondType = types.get(1);
 assertEquals(List.class, secondType.getRawClass());
 JavaType secondGenericType = secondType.getGenericType();
 assertEquals(List.class, secondGenericType.getRawClass());
 assertEquals(String.class, secondGenericType.getGeneric(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypesWithUnexpectedToken() throws Exception {
 parser = new TypeParser(factory);
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>,java.util.List<java.lang.String>x");
 parser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindClass() throws Exception {
 String className = "java.util.ArrayList";
 JavaType type = parser.parseType(new MyTokenizer(className));
 assertEquals(ArrayList.class, type.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassWithInvalidClassName() throws Exception {
 String className = "java.util.ArrayListx";
 parser.parseType(new MyTokenizer(className));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testProblem() {
 IllegalArgumentException problem = parser._problem(null, "Unexpected end-of-string");
 String msg = problem.getMessage();
 assertTrue(msg.contains("Failed to parse type"));
 assertTrue(msg.contains("Unexpected end-of-string"));
 }
}