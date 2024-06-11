package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private static TypeFactory typeFactory = new TypeFactory();
 private static TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParse() throws Exception {
 JavaType javaType = typeParser.parse("int");
 assertEquals(JavaType.getType(int.class), javaType);
 JavaType javaTypeArray = typeParser.parse("int[]");
 assertEquals(JavaType.getType(int[].class), javaTypeArray);
 JavaType javaTypeList = typeParser.parse("java.util.List<java.lang.Integer>");
 TypeReference<List<Integer>> typeReference = new TypeReference<List<Integer>>() {};
 assertEquals(typeReference.getType(), javaTypeList);
 JavaType javaTypeMap = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
 TypeReference<Map<String, Integer>> typeReferenceMap = new TypeReference<Map<String, Integer>>() {};
 assertEquals(typeReferenceMap.getType(), javaTypeMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException1() throws Exception {
 String canonical = "";
 try {
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException2() throws Exception {
 String canonical = "hello";
 try {
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException3() throws Exception {
 String canonical = "[java.util.List<java.lang.Integer>]";
 try {
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException4() throws Exception {
 String canonical = "java.util.List<java.lang.Integer, int[]>";
 try {
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalArgumentException5() throws Exception {
 String canonical = "java.util.List<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>>";
 try {
 JavaType javaType = typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseType() throws Exception {
 MyTokenizer tokens = new MyTokenizer("int");
 JavaType javaType = typeParser.parseType(tokens);
 assertEquals(JavaType.getType(int.class), javaType);

 tokens = new MyTokenizer("int[]");
 javaType = typeParser.parseType(tokens);
 assertEquals(JavaType.getType(int[].class), javaType);

 tokens = new MyTokenizer("java.util.List<java.lang.Integer>");
 List<JavaType> parameterTypes = Arrays.asList(JavaType.getType(int.class));
 TypeBindings b = TypeBindings.create(JavaType.getType(List.class), parameterTypes);
 javaType = typeParser._factory._fromClass(null, JavaType.getType(List.class), b);
 assertEquals(javaType, typeParser.parse("java.util.List<java.lang.Integer>"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeIllegalArgumentException1() throws Exception {
 MyTokenizer tokens = new MyTokenizer("");
 try {
 JavaType javaType = typeParser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeIllegalArgumentException2() throws Exception {
 MyTokenizer tokens = new MyTokenizer("hello");
 try {
 JavaType javaType = typeParser.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseTypes() throws Exception {
 MyTokenizer tokens = new MyTokenizer("int, int[]");List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 List<JavaType> expectedTypes = Arrays.asList(JavaType.getType(int.class), JavaType.getType(int[].class));
 assertEquals(expectedTypes, javaTypes);

 tokens = new MyTokenizer("java.util.List<java.lang.Integer>, java.util.List<java.lang.Integer>");
 javaTypes = typeParser.parseTypes(tokens);
 assertEquals(expectedTypes, javaTypes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypesIllegalArgumentException1() throws Exception {
 MyTokenizer tokens = new MyTokenizer("");
 try {
 List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypesIllegalArgumentException2() throws Exception {
 MyTokenizer tokens = new MyTokenizer("hello");
 try {
 List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypesIllegalArgumentException3() throws Exception {
 MyTokenizer tokens = new MyTokenizer("int, java.util.List<java.lang.Integer>");
 try {
 List<JavaType> javaTypes = typeParser.parseTypes(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindClass() throws Exception {
 JavaType javaType = typeParser.parse("java.util.HashMap<java.lang.String, java.lang.Integer>");
 Class<?> base = typeParser.findClass("java.util.Map", tokens);
 assertEquals(JavaType.getType(Map.class), javaType);
 assertEquals(Map.class, base);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassIllegalArgumentException1() throws Exception {
 MyTokenizer tokens = new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
 try {
 Class<?> base = typeParser.findClass("java.util.ArrayList", tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}