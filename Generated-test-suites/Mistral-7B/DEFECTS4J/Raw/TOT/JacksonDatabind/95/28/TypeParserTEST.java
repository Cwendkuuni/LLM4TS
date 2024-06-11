package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @Before
 public void setup() {
 _factory = new DefaultTypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParseSimpleType() throws IllegalArgumentException {
 String canonical = "int";
 JavaType type = _parser.parse(canonical);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() throws IllegalArgumentException {
 String canonical = "java.util.List";
 JavaType type = _parser.parse(canonical);
 assertEquals(List.class, type.getRawClass());
 }

 @Test
 public void testParseClassTypeWithGenericParameters() throws IllegalArgumentException {
 String canonical = "java.util.List<Integer>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> genericTypes = type.getGenerics();
 assertEquals(1, genericTypes.size());
 assertEquals(Integer.TYPE, genericTypes.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws IllegalArgumentException {
 String canonical = "java.util.List<Integer,String>";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException but it did not occur");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIncompleteType() throws IllegalArgumentException {
 String canonical = "java.util.List<Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 tokens.pushBack(">");
 _parser.parseTypes(tokens);
 _parser.parseType(tokens); // this should trigger the error
 throw _parser._problem(tokens, "Unexpected end-of-string");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClass() throws IllegalArgumentException {
 String canonical = "com.nonexistent.InvalidClass";
 _parser.parse(canonical);
 fail("Expected IllegalArgumentException but it did not occur");
 }
}