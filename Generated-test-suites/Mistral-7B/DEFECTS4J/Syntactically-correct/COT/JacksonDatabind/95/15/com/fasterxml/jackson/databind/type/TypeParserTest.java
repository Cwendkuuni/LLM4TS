package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.MyTokenizer;

public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @Before
 public void setUp() {
 _factory = new TypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse_simpleType() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParse_classType() throws Exception {
 String canonical = "java.util.ArrayList";
 JavaType expected = _factory.constructType(ArrayList.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParse_invalidType() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>asd";
 try {
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'asd' (remaining: 'asd'): Unexpected end-of-string: Unexpected token 'asd'";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParse_emptyType() throws Exception {
 String canonical = "";
 JavaType actual = _parser.parse(canonical);
 assertNull(actual);
 }

 @Test
 public void testParse_singleTokenType() throws Exception {
 String canonical = "java.lang.String";
 JavaType expected = _factory.constructType(String.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParse_typeWithGenerics_oneParameter() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expected = TypeBindings.typeBindingsFromString(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParse_typeWithGenerics_multipleParameters() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType expected = _factory.constructType(Map.class, String.class, List.class, Integer.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_typeWithMissingCloseBracket() throws Exception {
 String canonical = "java.util.List<java.lang.Integer<";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_typeWithMissingComma() throws Exception {
 String canonical = "java.util.List<java.lang.Integer,java.lang.String>java.lang.Integer";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParse_typeWithInvalidToken() throws Exception {
 String canonical = "java.util.List<java.lang.Integerasd>";
 try {
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Failed to parse type 'asd' (remaining: 'asd'): Unexpected token 'asd'";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseType_simpleType() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType actual = _parser.parseType(tokens);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParseType_classType() throws Exception {
 String canonical = "java.util.ArrayList";
 MyTokenizer tokens = new MyTokenizer(canonical);
 Class<?> expected = ArrayList.class;
 Class<?> actual = _parser.parseType(tokens).getRawClass();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseType_typeWithGenerics_oneParameter() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType actual = _parser.parseType(tokens);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParseType_typeWithGenerics_multipleParameters() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType expected = _factory.constructType(Map.class, String.class, List.class, Integer.class);
 JavaType actual = _parser.parseType(tokens);
 assertEquals(expected.getTypeId(), actual.getTypeId());
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_typeWithMissingCloseBracket() throws Exception {
 String canonical = "java.util.List<java.lang.Integer<";
 MyTokenizer tokens = new MyTokenizer(canonical);
 try {
 _parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class '': Unexpected '['";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_typeWithMissingComma() throws Exception {
 String canonical = "java.util.List<java.lang.Integer,java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 try {
 _parser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'Integer': Expected ',' or '>')";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseTypes_simpleType() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType[] actual = _parser.parseTypes(tokens).toArray(new JavaType[0]);
 assertEquals(expected, actual[0]);
 }

 @Test
 public void testParseTypes_classType() throws Exception {
 String canonical = "java.util.ArrayList";
 MyTokenizer tokens = new MyTokenizer(canonical);
 Class<?> expected = ArrayList.class;
 Class<?>[] actual = _parser.parseTypes(tokens).toArray(new Class<?>[0]);
 assertEquals(expected, actual[0]);
 }

 @Test
 public void testParseTypes_typeWithGenerics_oneParameter() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType[] actual = _parser.parseTypes(tokens).toArray(new JavaType[0]);
 assertEquals(expected, actual[0]);
 }

 @Test
 public void testParseTypes_typeWithGenerics_multipleParameters() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 JavaType[] expected = new JavaType[] { String.class, _factory.constructType(Collections.class, Integer.class) };
 JavaType[] actual = _parser.parseTypes(tokens).toArray(new JavaType[0]);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_typeWithMissingCloseBracket() throws Exception {
 String canonical = "java.util.List<java.lang.Integer<";
 MyTokenizer tokens = new MyTokenizer(canonical);
 try {
 _parser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class '': Unexpected '['";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_typeWithMissingComma() throws Exception {
 String canonical = "java.util.List<java.lang.Integer,java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 try {
 _parser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'Integer': Expected ',' or '>')";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testFindClass_simpleType() throws Exception {
 String className = "java.util.List<java.lang.Integer>";
 JavaType expected = _factory.constructType(Collections.class, Integer.class);
 JavaType actual = _parser.parse(canonical);
 Class<?> actualClass = _parser.findClass(expected.getTypeName(), tokens);
 assertEquals(expected.getRawClass(), actualClass);
 }

 @Test
 public void testFindClass_classType() throws Exception {
 String className = "java.util.ArrayList";
 Class<?> expected = ArrayList.class;
 Class<?> actual = _parser.findClass(className, tokens);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_invalidClassName() throws Exception {
 String className = "java.util.List<java.lang.InvalidClassName>";
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>");
 try {
 _parser.findClass(className, tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'InvalidClassName'";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }
}