package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import org.junit.Assert.*;

public class TypeParserTest {

 private TypeFactory typeFactory = new DefaultTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType parsedType = typeParser.parse(canonical);
 TypeReference expectedType = new TypeReference() {}.getType();
 Assert.assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.util.ArrayList";
 JavaType parsedType = typeParser.parse(canonical);
 TypeReference expectedType = new TypeReference("java.util.ArrayList", 0) {}.getType();
 Assert.assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseGenericType() {
 String canonical = "java.util.ArrayList<java.lang.Integer>";
 JavaType parsedType = typeParser.parse(canonical);
 TypeReference expectedType = new TypeReference<ArrayList<Integer>>() {}.getType();
 Assert.assertEquals(expectedType, parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.util.ArrayList<java.lang.Integer, java.lang.String>";
 typeParser.parse(canonical); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyType() {
 String canonical = "";
 typeParser.parse(canonical); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMalformedType() {
 String canonical = "java.util.ArrayList<java.lang.Integer<java.lang.String>";
 typeParser.parse(canonical); // should throw an IllegalArgumentException
 }
}