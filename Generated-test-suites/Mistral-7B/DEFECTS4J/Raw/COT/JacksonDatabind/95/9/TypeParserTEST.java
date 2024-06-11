package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTyper;

/**
 * JUnit test cases for TypeParser class.
 */
public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 _factory = new TypeFactory() {
 @Override
 public JavaType _fromClass(Class<?> base, Class<?> implementation, TypeBindings bindings) {
 return new DefaultTyper().constructType(base, bindings);
 }
 };
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse() throws Exception {
 String canonicalType1 = "java.util.List<java.lang.String>";
 JavaType expectedType1 = JavaType.constructParametrizedType(List.class, String.class);
 JavaType actualType1 = _parser.parse(canonicalType1);
 assertEquals(expectedType1, actualType1);

 String canonicalType2 = "java.util.ArrayList<java.util.LinkedList<java.util.ArrayList<Object>>>";
 JavaType expectedType2 = JavaType.constructParametrizedType(ArrayList.class, ArrayList.class, Object.class);
 JavaType actualType2 = _parser.parse(canonicalType2);
 assertEquals(expectedType2, actualType2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidInput() {
 _parser.parse("InvalidTypeInput");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithUnexpectedTokens() {
 String canonicalType3 = "java.util.List<java.lang.String> unexpectedToken";
 _parser.parse(canonicalType3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingEndToken() {
 String canonicalType4 = "java.util.List<java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonicalType4.trim());
 _parser.parseType(tokens);
 tokens.nextToken(); // consume '>'
 tokens.pushBack(">"); // pushback for assertion
 _parser.parseTypes(tokens);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassWithInvalidClassName() {
 String className = "InvalidClassName";
 _parser.findClass(className, new MyTokenizer(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassWithClassNotFound() {
 String className = "java.lang.MyNonExistentClass";
 _parser._problem(new MyTokenizer(""), "Class not found: " + className);
 }

}