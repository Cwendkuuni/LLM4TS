package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Test
 public void testTypeParserInitialization() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_ValidType() throws Exception {
 String canonicalType = "java.util.List<com.fasterxml.jackson.databind.JavaType>";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertNotNull(parsedType);
 assertEquals("java.util.List", parsedType.getRawClass().getName());
 }

 @Test
 public void testParse_InvalidType() throws Exception {
 String invalidType = "invalidType";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parse(invalidType));
 }

 @Test
 public void testParseType_ValidClass() throws Exception {
 String className = "java.util.List";
 JavaType parsedType = typeParser.parseType(new MyTokenizer(className));
 assertNotNull(parsedType);
 assertEquals("java.util.List", parsedType.getRawClass().getName());
 }

 @Test
 public void testParseType_ClassWithGenerics() throws Exception {
 String className = "java.util.List<com.fasterxml.jackson.databind.JavaType>";
 JavaType parsedType = typeParser.parseType(new MyTokenizer(className));
 assertNotNull(parsedType);
 assertEquals("java.util.List", parsedType.getRawClass().getName());
 List<JavaType> actualTypeArguments = parsedType.getTypeParameters();
 assertEquals(1, actualTypeArguments.size());
 JavaType expectedTypeArgument = typeFactory.constructType(JavaType.TypeReference.construct(com.fasterxml.jackson.databind.JavaType.class));
 assertEquals(expectedTypeArgument, actualTypeArguments.get(0));
 }

 @Test
 public void testParseTypes_ValidTypes() throws Exception {
 String canonicalTypes = "java.util.List<com.fasterxml.jackson.databind.JavaType>, java.util.Map<java.lang.String, java.lang.Integer>";
 List<JavaType> parsedTypes = typeParser.parseTypes(new MyTokenizer(canonicalTypes));
 List<JavaType> expectedTypes = Arrays.asList(
         typeFactory.constructType(JavaType.TypeReference.construct(JavaType.class)),
         typeFactory.constructType(JavaType.TypeReference.construct(JavaType.class).parameterize(String.class, Integer.class))
 );
 assertEquals(expectedTypes, parsedTypes);
 }

 @Test
 public void testParseTypes_UnexpectedEndOfString() throws Exception {
 String canonicalTypes = "java.util.List<com.fasterxml.jackson.databind.JavaType>";
 MyTokenizer tokens = new MyTokenizer(canonicalTypes.trim());
 typeParser.parseTypes(tokens);
 tokens.nextToken(); // consume '>'
 assertThrows(IllegalArgumentException.class, () -> tokens.nextToken());
 }

 @Test
 public void testFindClass_ValidClassName() throws Exception {
 String validClassName = "java.util.List";
 JavaType parsedType = typeParser.parseType(new MyTokenizer(validClassName));
 assertEquals(Class.forName(validClassName), parsedType.getRawClass());
 }

 @Test
 public void testFindClass_InvalidClassName() throws Exception {
 String invalidClassName = "invalidClass";
 assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(new MyTokenizer(invalidClassName)));
 }

 @Test
 public void test_Problem_UnexpectedEndOfString() throws Exception {
 MyTokenizer tokens = new MyTokenizer("type");
 typeParser._problem(tokens, "Unexpected end-of-string");
 String actualMessage = tokens.getAllInput();
 String expectedMessage = "Failed to parse type 'type' (remaining: ''): Unexpected end-of-string";
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void test_Problem_UnexpectedToken() throws Exception {
 MyTokenizer tokens = new MyTokenizer("type, x");
 typeParser._problem(tokens, "Unexpected token 'x'");
 String actualMessage = tokens.getAllInput();
 String expectedMessage = "Failed to parse type 'type' (remaining: 'x'): Unexpected token 'x'";
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void test_Problem_UnexpectedToken_ExpectedTypesOrGreaterThan() throws Exception {
 MyTokenizer tokens = new MyTokenizer("type<");
 typeParser._problem(tokens, "Unexpected token '<'");
 String actualMessage = tokens.getAllInput();
 String expectedMessage = "Failed to parse type 'type' (remaining: '<<'): Unexpected token '<'";
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void test_Problem_UnexpectedToken_ExpectedCommaOrGreaterThan() throws Exception {
 MyTokenizer tokens = new MyTokenizer("type>, type2");
 typeParser._problem(tokens, "Unexpected token '>'");
 String actualMessage = tokens.getAllInput();
 String expectedMessage = "Failed to parse type 'type' (remaining: '>type2'): Unexpected token '>'";
 assertEquals(expectedMessage, actualMessage);
 }
}