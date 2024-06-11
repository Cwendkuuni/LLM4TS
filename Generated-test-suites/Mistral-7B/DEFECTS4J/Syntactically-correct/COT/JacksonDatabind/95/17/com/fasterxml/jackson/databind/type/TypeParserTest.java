package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactoryBuilder().build();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParse_simpleType() {
 JavaType result = typeParser.parse("int");
 JavaType expected = typeFactory.constructType(int.class);
 assertEquals(expected, result);
 }

 @Test
 public void testParse_classType() {
 JavaType result = typeParser.parse("java.util.List");
 JavaType expected = typeFactory.constructType(List.class);
 assertEquals(expected, result);
 }

 @Test
 public void testParse_genericType() {
 JavaType result = typeParser.parse("java.util.List<Integer>");
 JavaType expected = TypeBindings.constructParametrizedType(List.class, int.class);
 assertEquals(expected, result);
 }

 @Test
 public void testParse_nestedGenericsType() {
 JavaType result = typeParser.parse("java.util.List<List<Integer>>");
 JavaType expected = TypeBindings.constructParametrizedType(List.class, List.class, int.class);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType() {
 String invalidType = "java.util.List<abc";
 typeParser.parse(invalidType);
 fail("Must throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingEndToken() {
 String missingEndToken = "java.util.List<Integer>";
 MyTokenizer tokens = new MyTokenizer(missingEndToken);
 typeParser.parseType(tokens);
 fail("Must throw IllegalArgumentException");
 }

 @Test
 public void testParseTypes_simpleTypes() {
 List<JavaType> result = typeParser.parseTypes("int, long, java.util.List<String>");
 List<JavaType> expected = Arrays.asList(typeFactory.constructType(int.class), typeFactory.constructType(long.class),
 typeFactory.constructType(List.class), typeFactory.constructType(String.class));
 assertEquals(expected, result);
 }

 @Test
 public void testParseTypes_typesWithGenerics() {
 List<JavaType> result = typeParser.parseTypes("java.util.List<java.util.List<Integer>>, java.util.List<int[]>");
 List<JavaType> expected = Arrays.asList(TypeBindings.constructParametrizedType(List.class, List.class, int.class),
 TypeBindings.constructParametrizedType(List.class, int[].class));
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_nonExistingClass() {
 String nonExistingClass = "com.nonExisting.Class";
 typeParser.findClass(nonExistingClass, null);
 fail("Must throw IllegalArgumentException");
 }

 @Test
 public void testFindClass_existingClass() {
 String existingClass = "java.util.List";
 JavaType javaUtilList = typeParser.parse(existingClass);
 Class<?> clazz = javaUtilList.getRawClass();
 assertEquals(List.class, clazz);
 }
}