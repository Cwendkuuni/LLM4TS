package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory factory = new TypeFactory();
 private TypeParser parser = new TypeParser(factory);

 @Test
 public void testParse() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType type = parser.parse(canonical);
 assertEquals(List.class, type.getRawClass());
 assertEquals(String.class, type.getContentType());
 assertNotNull(type.getTypeParams());
 assertEquals(1, type.getTypeParams().size());
 assertEquals(String.class, type.getTypeParams().get(0));
 }

 @Test
 public void testParseEmpty() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> parser.parse(""));
 }

 @Test
 public void testParseInvalidCanonical() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<java.lang.String>java.lang.String"));
 }

 @Test
 public void testParseInvalidClass() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<java.lang.String>x.y.Z"));
 }

 @Test
 public void testParseWrongToken() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String><");
 assertThrows(IllegalArgumentException.class, () -> parser.parseType(tokens));
 }

 @Test
 public void testParseTypes() throws Exception {
 String canonical = "java.util.List<java.lang.String>," +
         "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType type = parser.parse(canonical);
 List<JavaType> typeParams = type.getTypeParams();
 assertEquals(2, typeParams.size());
 assertEquals(String.class, typeParams.get(0));
 assertEquals(Integer.class, typeParams.get(1));
 }

 @Test
 public void testParseTypesEmpty() throws Exception {
 JavaType type = parser.parse("java.util.List<>");
 assertNotNull(type.getTypeParams());
 assertEquals(0, type.getTypeParams().size());
 }

 @Test
 public void testParseTypesNoTypes() throws Exception {
 JavaType type = parser.parse("java.util.List");
 assertNull(type.getTypeParams());
 }

 @Test
 public void testParseTypesUnclosedBracket() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
 tokens.pushBack(">");
 JavaType type = parser.parseType(tokens);
 assertNotNull(type.getTypeParams());
 assertEquals(0, type.getTypeParams().size());
 }

 @Test
 public void testParseTypesMultipleTypes() throws Exception {
 String canonical = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType type = parser.parse(canonical);
 List<JavaType> typeParams = type.getTypeParams();
 assertEquals(2, typeParams.size());
 assertEquals(String.class, typeParams.get(0));
 assertEquals(Integer.class, typeParams.get(1));
 }

 @Test
 public void testParseTypesMixedTypesAndEmptyList() throws Exception {
 String canonical = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>, java.util.List<>";
 JavaType type = parser.parse(canonical);
 List<JavaType> typeParams = type.getTypeParams();
 assertEquals(3, typeParams.size());
 assertEquals(String.class, typeParams.get(0));
 assertEquals(Integer.class, typeParams.get(1));
 assertNull(typeParams.get(2));
 }

 @Test
 public void testFindClass() throws Exception {
 Class<?> clazz = parser.findClass("java.util.List");
 assertEquals(List.class, clazz);
 }

 @Test
 public void testFindClassInvalidClass() throws Exception {
 assertThrows(IllegalArgumentException.class, () -> parser.findClass("x.y.Z"));
 }

 @Test
 public void testProblem() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>abc");
 IllegalArgumentException exception = parser._problem(tokens, "Unexpected token 'abc'", Arrays.asList("java.util.List<java.lang.String>", "abc"));
 assertEquals("Failed to parse type 'java.util.List<java.lang.String>abc' (remaining: 'abc'): Unexpected token 'abc'", exception.getMessage());
 }

}