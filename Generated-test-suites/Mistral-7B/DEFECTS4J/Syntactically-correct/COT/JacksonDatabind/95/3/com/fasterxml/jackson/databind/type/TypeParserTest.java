package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;

/**
 * Test class for TypeParser.
 */
public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @BeforeClass
 public static void setupClass() {
 _factory = new TypeFactoryBuilder().build();
 }

 @Before
 public void setUp() {
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse_validType() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 JavaType type = _parser.parse(canonical);
 Assert.assertNotNull(type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>foo";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_emptyType() throws Exception {
 String canonical = "java.util.List<>";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingEndToken() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseType_validType() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType type = _parser.parseType(tokens);
 assertNotNull(type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_invalidToken() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>foo";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_missingEndToken() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTypes_validTypes() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>, " +
         "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 List<JavaType> types = _parser.parseTypes(tokens);
 assertNotNull(types);
 assertEquals(2, types.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_missingEndToken() throws Exception {
 String canonical = "java.util.List<com.fasterxml.jackson.databind.JsonNode>, ";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindClass() throws Exception {
 String className = "java.util.List";
 JavaType type = _parser._factory._fromClass(null, Class.forName(className), null);
 Class<?> clazz = _parser.findClass(className, null);
 assertSame(type.getRawClass(), clazz);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_nonExistentClass() throws Exception {
 String className = "com.nonexistent.package.NonExistentClass";
 _parser.findClass(className, null);
 fail("Expected IllegalArgumentException");
 }

}