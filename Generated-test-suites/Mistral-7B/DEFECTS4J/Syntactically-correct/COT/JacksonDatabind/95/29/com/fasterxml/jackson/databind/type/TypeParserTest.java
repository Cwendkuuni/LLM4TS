package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Test class for TypeParser
 */
public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @BeforeClass
 public static void setup() {
 _factory = new TypeFactory();
 }

 @Before
 public void setupParser() {
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse_nullCanonical() {
 try {
 _parser.parse(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse_emptyCanonical() {
 try {
 JavaType result = _parser.parse("");
 assertEquals(JavaType.getType(Void.TYPE), result);
 } catch (IllegalArgumentException e) {
 fail("Unexpected error: " + e.getMessage());
 }
 }

 @Test
 public void testParse_validCanonical() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 try {
 JavaType result = _parser.parse(canonical);
 JavaType expected = new TypeFactory()
 .constructType(new TypeReference<List<JsonNode>>() {}.getType());
 assertEquals(expected, result);
 } catch (IllegalArgumentException e) {
 fail("Unexpected error: " + e.getMessage());
 }
 }

 @Test
 public void testParse_invalidCanonical() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode<>";
 try {
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse_invalidToken() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode#>";
 try {
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'java.util.List<com.fasterxml.jackson.databind.JsonNode#>' (remaining: '')";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testParseType_nullCanonical() {
 try {
 _parser.parseType(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseType_emptyCanonical() {
 JavaType result = _parser.parseType(new MyTokenizer(""));
 assertEquals(JavaType.getType(Void.TYPE), result);
 }

 @Test
 public void testParseType_validClass() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType result = _parser.parseType(tokens);
 JavaType expected = TypeBindings.typeBindingFromClass(JsonNode.class)
 .withoutErasures()
 .withGenericTypeArguments(List.class, JsonNode.class)
 .withRawType(List.class);
 assertEquals(expected, result);
 }

 @Test
 public void testParseType_invalidToken() {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode#>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 try {
 _parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'java.util.List<com.fasterxml.jackson.databind.JsonNode#>' (remaining: '')";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testParseTypes_noTypes() {
 MyTokenizer tokens = new MyTokenizer("<>");
 try {
 List<JavaType> result = _parser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token '<'";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testParseTypes_oneType() {
 MyTokenizer tokens = new MyTokenizer("<java.util.List>");
 List<JavaType> result = _parser.parseTypes(tokens);
 JavaType expected = TypeBindings.typeBindingFromClass(List.class)
 .withoutErasures();
 assertEquals(Arrays.asList(expected), result);
 }

 @Test
 public void testParseTypes_multipleTypes() {
 MyTokenizer tokens = new MyTokenizer("<java.util.List<java.util.Map<java.lang.String,java.lang.Integer>>,java.util.ArrayList>");
 List<JavaType> result = _parser.parseTypes(tokens);
 JavaType expectedListType = TypeBindings.typeBindingFromClass(List.class)
 .withoutErasures();
 JavaType expectedMapType = TypeBindings.typeBindingFromClass(Map.class)
 .withTypeParameters(new TypeReference<Map<String, Integer>>() {}.getType())
 .withoutErasures();
 JavaType expectedArrayListType = TypeBindings.typeBindingFromClass(ArrayList.class)
 .withoutErasures();
 List<JavaType> expected = Arrays.asList(expectedListType, expectedMapType, expectedArrayListType);
 assertEquals(expected, result);
 }

 @Test
 public void testFindClass_validClassName() {
 String className = "java.util.ArrayList";
 JavaType result = _parser.findClass(className, new MyTokenizer(className));
 JavaType expected = TypeBindings.typeBindingFromClass(ArrayList.class)
 .withoutErasures();
 assertEquals(expected, result);
 }

 @Test
 public void testFindClass_invalidClassName() {
 String className = "java.util.ArrayList<";
 MyTokenizer tokens = new MyTokenizer(className);
 try {
 _parser.findClass(className, tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'java.util.ArrayList<'";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testFindClass_classNotFound() {
 String className = "com.non.existent.Package.NonExistentClass";
 MyTokenizer tokens = new MyTokenizer(className);
 try {
 _parser.findClass(className, tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'com.non.existent.Package.NonExistentClass'";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }
}