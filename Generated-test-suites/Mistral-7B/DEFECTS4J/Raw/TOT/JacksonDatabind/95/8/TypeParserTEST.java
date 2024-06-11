package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

 private TypeFactory _factory = new TypeFactory(); // assumes TypeFactory is available
 private TypeParser _parser;

 @Test
 public void testParseSimpleType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("int");
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("java.lang.String");
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("int[]");
 assertEquals(int[].class, type.getRawClass());
 }

 @Test
 public void testParseParameterizedType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("java.util.List<java.lang.String>");
 List<String> list = (List<String>) _factory.constructType(type);
 assertEquals(ArrayList.class, list.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 _parser = new TypeParser(_factory);
 _parser.parse("invalidType"); // should throw IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 _parser = new TypeParser(_factory);
 _parser.parse("java.util.List<java.lang.String<"); // should throw IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseExtraToken() {
 _parser = new TypeParser(_factory);
 _parser.parse("java.util.List<java.lang.String> x"); // should throw IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidToken() {
 _parser = new TypeParser(_factory);
 _parser.parse("java.util.List<java.lang.String x>"); // should throw IllegalArgumentException
 }

}