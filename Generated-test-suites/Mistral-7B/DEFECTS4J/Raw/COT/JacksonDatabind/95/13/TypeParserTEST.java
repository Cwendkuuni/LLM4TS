package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.MyTokenizer;

/**
 * Test class for TypeParser
 */
public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<String>>() {});
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>X");
 typeParser.parseType(tokens);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedEndOfString() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
 typeParser.parseType(tokens);
 tokens.nextToken();
 typeParser.parseType(tokens);
 }

 @Test
 public void testParseClass() {
 String className = "java.util.List";
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<?>>() {});
 MyTokenizer tokens = new MyTokenizer(className);
 JavaType actualType = typeParser.parseType(tokens);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseClassNotExist() {
 String className = "com.nonexist.MyClass";
 MyTokenizer tokens = new MyTokenizer(className);
 typeParser.parseType(tokens);
 }

 @Test
 public void testParseGenericClass() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType expectedType = TypeBindings.construct(List.class, String.class);
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 JavaType actualType = typeParser.parseType(tokens);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidGenericType() {
 String canonicalType = "java.util.List<java.lang.StringX>";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 typeParser.parseType(tokens);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMismatchedClosingBracket() {
 String canonicalType = "java.util.List<java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonicalType + ">"); // miss an extra '>'
 typeParser.parseType(tokens);
 }

 @Test
 public void testParseMultipleTypes() {
 String canonicalType = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType expectedType = TypeBindings.construct(List.class, String.class, Map.class, String.class, Integer.class);
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 JavaType actualType = typeParser.parseType(tokens);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 String canonicalType = "java.util.List<java.lang.String, java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 typeParser.parseType(tokens);
 }

 @Test
 public void testParseEmpty() {
 MyTokenizer tokens = new MyTokenizer("");
 JavaType actualType = typeParser.parseType(tokens);
 assertNull(actualType);
 }

 @Test
 public void testParseSingleType() {
 String canonicalType = "java.lang.String";
 JavaType expectedType = typeFactory.constructType(String.class);
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 JavaType actualType = typeParser.parseType(tokens);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseTypeParserWithFactory() {
 TypeParser typeParserWithFactory = new TypeParser(typeFactory);
 JavaType actualType = typeParserWithFactory.parse("java.util.List<java.lang.String>");
 JavaType expectedType = typeFactory.constructType(new TypeReference<List<String>>() {});
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseTypeParserWithFactoryAndGeneric() {
 TypeParser typeParserWithFactory = new TypeParser(typeFactory);
 JavaType actualType = typeParserWithFactory.parse("java.util.List<java.lang.String>");
 JavaType expectedType = TypeBindings.construct(List.class, String.class);
 assertEquals(expectedType, actualType);
 }

}