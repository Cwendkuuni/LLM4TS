package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 typeFactory = new TypeFactoryBuilder()
 .build();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() throws Exception {
 String canonical = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructType(canonical);
 assertEquals(expectedType.getRawClass(), parsedType.getRawClass());
 }

 @Test
 public void testParseParameterizedType() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = TypeBindings.mapType(typeFactory.constructType(canonical.substring(0, canonical.lastIndexOf("<"))),
 new Type[] { typeFactory.constructType("java.lang.String") });
 assertEquals(expectedType, parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMalformedCanonicalType() throws Exception {
 String canonical = "java.util.List<java.lang.String";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedTokenAfterType() throws Exception {
 String canonical = "java.util.List<java.lang.String<foo>";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedEndOfString() throws Exception {
 String canonical = "java.util.List<java.lang.String";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTypeWithGenericsAndCommaSeparatedTypes() throws Exception {
 String canonical = "java.util.List<java.lang.String, java.util.ArrayList<java.lang.Integer>>";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = TypeBindings.mapType(typeFactory.constructType(canonical.substring(0, canonical.lastIndexOf("<"))),
 new Type[] { typeFactory.constructType("java.lang.String"), typeFactory.constructType("java.util.ArrayList<java.lang.Integer>") });
 assertEquals(expectedType, parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingAngleBracket() throws Exception {
 String canonical = "java.util.List<java.lang.String<";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseEmptyListType() throws Exception {
 String canonical = "java.util.List<>";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructType(canonical);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseArrayType() throws Exception {
 String canonical = "int[]";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructArrayType(Integer.TYPE);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseArrayTypeWithBrackets() throws Exception {
 String canonical = "int[][]";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructArrayType(typeFactory.constructArrayType(Integer.TYPE));
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseArrayTypeWithDimensionsAndBrackets() throws Exception {
 String canonical = "int[][][]";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructArrayType(typeFactory.constructArrayType(typeFactory.constructArrayType(Integer.TYPE)));
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParsePrimitiveType() throws Exception {
 String canonical = "int";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructType(canonical);
 assertEquals(expectedType.getRawClass(), parsedType.getRawClass());
 }

 @Test
 public void testParsePrimitiveTypeWithArray() throws Exception {
 String canonical = "int[]";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructArrayType(Integer.TYPE);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseTypeWithAnnotations() throws Exception {
 String canonical = "@MyAnnotation java.util.List<java.lang.String>";
 JavaType parsedType = typeParser.parse(canonical);
 JavaType expectedType = typeFactory.constructType(canonical);
 assertEquals(expectedType, parsedType);
 }
}