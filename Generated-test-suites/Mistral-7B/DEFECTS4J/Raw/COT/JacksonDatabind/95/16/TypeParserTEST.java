package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_validType() {
 String canonicalType = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonicalType);
 assertEquals(List.class, javaType.getRawClass());
 assertEquals(String.class, javaType.getComponentType());
 }

 @Test
 public void testParse_validNonGenericType() {
 String canonicalType = "java.util.Date";
 JavaType javaType = typeParser.parse(canonicalType);
 assertEquals(Date.class, javaType.getRawClass());
 }

 @Test
 public void testParse_emptyString() {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(""));
 }

 @Test
 public void testParse_nullInput() {
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(null));
 }

 @Test
 public void testParseType_validType() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
 JavaType javaType = typeParser.parseType(tokens);
 assertEquals(List.class, javaType.getRawClass());
 assertEquals(String.class, javaType.getComponentType());
 }

 @Test
 public void testParseType_validNonGenericType() {
 MyTokenizer tokens = new MyTokenizer("java.util.Date");
 JavaType javaType = typeParser.parseType(tokens);
 assertEquals(Date.class, javaType.getRawClass());
 }

 @Test
 public void testParseType_endOfInput() {
 MyTokenizer tokens = new MyTokenizer("java.util.List");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens));
 }

 @Test
 public void testParseTypes_validTypes() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>,java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>");
 List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 assertEquals(2, javaTypes.size());
 assertEquals(String.class, javaTypes.get(0).getRawClass().getComponentType());
 assertEquals(List.class, javaTypes.get(0).getRawClass().getComponentType().getComponentType());
 assertEquals(String.class, javaTypes.get(1).getRawClass().getComponentType().getComponentType().getComponentType());
 assertEquals(Integer.class, javaTypes.get(1).getRawClass().getComponentType().getComponentType().getComponentType());
 }

 @Test
 public void testParseTypes_endOfInput() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>,java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>,");
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens));
 }

 @Test
 public void testFindClass_validClassName() {
 String className = "java.util.List";
 JavaType javaType = typeParser.parseType(tokensForClassName(className));
 assertEquals(List.class, javaType.getRawClass());
 }

 @Test
 public void testFindClass_invalidClassName() {
 String className = "java.util.ListX";
 MyTokenizer tokens = tokensForClassName(className);
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens));
 }

 @Test
 public void testProblemMessage() {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String> (remaining: '>') : Failed to parse type 'java.util.List<java.lang.String> (remaining: '>') : Unexpected token '>'");
 IllegalArgumentException ex = typeParser._problem(tokens, "Unexpected token '>'");
 assertEquals("Failed to parse type 'java.util.List<java.lang.String> (remaining: '>') : Unexpected token '>', problem: Unexpected token '>'", ex.getMessage());
 }

 // Edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testParse_emptyList() {
 typeParser.parse("java.util.List< >");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_unmatchedGenerics() {
 typeParser.parse("java.util.List<java.lang.String, java.lang.Integer>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingClosingBracket() {
 typeParser.parse("java.util.List<java.lang.String> java.util.Map<java.lang.String, java.lang.String>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingComma() {
 typeParser.parse("java.util.List<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_genericTypeWithInvalidType() {
 typeParser.parse("java.util.List<java.lang.String, Integer>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidToken() {
 typeParser.parse("java.util.List<java.lang.String> @");
 }

 @Test
 public void testParse_invalidFactory() {
 assertThrows(IllegalArgumentException.class, () -> new TypeParser(null));
 }

 private MyTokenizer tokensForClassName(String className) {
 MyTokenizer tokens = new MyTokenizer(className);
 if (!tokens.hasMoreTokens()) {
 throw new IllegalArgumentException("Empty class name: " + className);
 }
 return tokens;
 }

}