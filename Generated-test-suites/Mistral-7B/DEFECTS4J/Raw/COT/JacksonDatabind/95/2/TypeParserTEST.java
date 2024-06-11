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

/**
 * Test cases for TypeParser class
 *
 * @author John Doe
 */
public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParse_validType() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeReference typeReference = new TypeReference<List<Integer>>() {};
 assertEquals(typeReference.getType(), javaType);
 }

 @Test
 public void testParse_emptyType() {
 String canonicalType = "";
 JavaType javaType = typeParser.parse(canonicalType);
 assertEquals(JavaType.NULL, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType() {
 String canonicalType = "invalidType";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParseType_validClass() {
 String className = "java.util.List";
 JavaType javaType = typeParser.parseType(new MyTokenizer(className));
 TypeReference typeReference = new TypeReference<List<?>>() {};
 assertEquals(typeReference.getType(), javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_invalidClass() {
 String className = "invalidClass";
 typeParser.parseType(new MyTokenizer(className));
 }

 @Test
 public void testParseTypes_validTypes() {
 String canonicalTypes = "java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.Integer>";
 List<JavaType> javaTypes = typeParser.parseTypes(new MyTokenizer(canonicalTypes));
 List<TypeReference<?>> typeReferences = Arrays.asList(new TypeReference<List<Integer>>() {},
 new TypeReference<Map<String, Integer>>() {});
 assertEquals(typeReferences, Arrays.asList(typeFactory.constructType(javaTypes.get(0)),
 typeFactory.constructType(javaTypes.get(1))));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_invalidTypes() {
 String canonicalTypes = "java.util.List<java.lang.Integer,java.lang.String>,java.util.Map<java.lang.String,java.lang.Integer>";
 typeParser.parseTypes(new MyTokenizer(canonicalTypes));
 }

 @Test
 public void testFindClass_validClass() {
 String className = "java.util.List";
 JavaType javaType = typeParser.parseType(new MyTokenizer(className));
 Class<?> clazz = typeFactory.constructType(javaType);
 assertEquals(List.class, clazz);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_invalidClass() {
 String className = "invalidClass";
 typeParser.parseType(new MyTokenizer(className));
 }

 @Test
 public void testProblem_validErrorMessage() {
 String canonicalType = "java.util.List<java.lang.Integer><";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 IllegalArgumentException exception = typeParser._problem(tokens, "Invalid token '<'");
 String expectedErrorMessage = String.format("Failed to parse type '%s' (remaining: '<'): Invalid token '<'",
 tokens.getAllInput());
 assertEquals(expectedErrorMessage, exception.getMessage());
 }
}