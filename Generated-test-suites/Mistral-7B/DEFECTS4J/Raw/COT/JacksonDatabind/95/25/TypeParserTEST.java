package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTyping;

/**
 * Test class for TypeParser.
 */
public class TypeParserTest {

 private static TypeFactory factory = new TypeFactory().defaultTyping(DefaultTyping.STATIC);
 private static TypeParser parser = new TypeParser(factory);

 @Test
 public void testParse_NullCanonicalForm() {
     try {
         parser.parse(null);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testParse_EmptyCanonicalForm() {
     try {
         parser.parse("");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testParse_SingleClassType() {
     JavaType result = parser.parse("java.lang.String");
     JavaType expected = factory.constructType(String.class);
     assertEquals(expected.getRawClass(), result.getRawClass());
 }

 @Test
 public void testParse_SingleClassTypeWithGenerics() {
     JavaType result = parser.parse("java.util.List<java.lang.String>");
     JavaType expected = TypeBindings.constructType(List.class, String.class);
     assertEquals(expected, result);
 }

 @Test
 public void testParse_MultipleTypesWithGenerics() {
     JavaType result = parser.parse("java.util.List<java.lang.String> java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>");
     JavaType expected = TypeBindings.constructType(List.class, String.class)
             .paramType(TypeBindings.constructType(Map.class, String.class, List.class, Integer.class));
     assertEquals(expected, result);
 }

 @Test
 public void testParse_InvalidCanonicalForm() {
     try {
         parser.parse("java.util.List<java.lang.String> java.util.Map<java.lang.String,java.util.List<java.lang.Integer> java.lang.String");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testParse_InvalidCanonicalFormWithUnexpectedToken() {
     try {
         parser.parse("java.util.List<java.lang.String> java.util.Map<java.lang.String,java.util.List<java.lang.Integer> java.lang.String> foo");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testFindClass_ValidClass() {
     JavaType result = parser.parse("java.lang.String");
     Class<?> expected = String.class;
     assertEquals(expected, result.getRawClass());
 }

 @Test
 public void testFindClass_InvalidClassName() {
     try {
         parser.parse("com.fasterxml.jackson.databind.type.TypeParserTestInvalidClassName");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }
}