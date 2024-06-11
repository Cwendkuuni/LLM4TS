package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeParserTest {

 private TypeFactory typeFactory = new DefaultTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalName = "java.lang.String";
 JavaType javaType = typeParser.parse(canonicalName);
 assertNotNull(javaType);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 String canonicalName = "[Ljava.lang.Integer;";
 JavaType javaType = typeParser.parse(canonicalName);
 assertNotNull(javaType);
 assertEquals(int[].class, javaType.getRawClass());
 }

 @Test
 public void testParseGenericType() {
 String canonicalName = "java.util.ArrayList<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalName);
 assertNotNull(javaType);
 TypeReference typeRef = new TypeReference<ArrayList<Integer>>() {};
 assertEquals(typeRef.getType(), javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalName = "InvalidType";
 try {
 JavaType javaType = typeParser.parse(canonicalName);
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'InvalidType' (remaining: ')', problem: Can not locate class 'InvalidType'", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseExtraTokens() {
 String canonicalName = "java.lang.String<";
 JavaType javaType = typeParser.parse(canonicalName);
 // this test should fail
 }
}