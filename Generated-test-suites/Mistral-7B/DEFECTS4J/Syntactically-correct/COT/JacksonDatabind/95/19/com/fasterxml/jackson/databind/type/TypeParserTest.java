package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTyping;

/**
 * Test cases for {@link TypeParser} class
 */
public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void init() {
 _factory = new TypeFactory().defaultImpl();
 }

 @Before
 public void setup() {
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse_validType() {
 String canonical = "java.lang.String";
 JavaType expected = _factory._fromClass(null, String.class, null);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse_validClassWithGenerics() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expected = TypeBindings.constructParametrizedType(List.class, Integer.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse_multipleTypesWithGenerics() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType expected = TypeBindings.constructParametrizedType(Map.class, String.class, List.class, Integer.class);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType() {
 String canonical = "invalidType";
 _parser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingClosingBracket() {
 String canonical = "java.util.List<java.lang.Integer<";
 _parser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingComma() {
 String canonical = "java.util.List<java.lang.Integer java.lang.String>";
 _parser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidClass() {
 String canonical = "java.invalidClass";
 _parser.parse(canonical);
 }
}