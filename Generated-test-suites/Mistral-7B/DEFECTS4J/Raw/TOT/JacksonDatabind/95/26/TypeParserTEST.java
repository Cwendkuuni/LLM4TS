package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 typeFactory = new TypeFactoryBuilder()
 .baseClass(Object.class)
 .build();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testParseSimpleType() throws Exception {
 JavaType javaType = new TypeParser(typeFactory).parse("int");
 assertEquals(JavaType.getCollectionType(null, List.class), javaType.getRawType());
 assertEquals(IntTypeIdentifier.class, javaType.getRawClass());
 }

 @Test
 public void testParseClassType() throws Exception {
 JavaType javaType = new TypeParser(typeFactory).parse("java.util.ArrayList");
 assertEquals(ArrayList.class, javaType.getRawClass());
 }

 @Test
 public void testParseGenericType() throws Exception {
 JavaType javaType = new TypeParser(typeFactory).parse("java.util.ArrayList<java.util.List<java.lang.Integer>>");
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(ArrayList.class, typeBindings.getRawClass());
 assertEquals(List.class, typeBindings.getGenericType().getRawClass());
 assertEquals(Integer.class, typeBindings.getGenericParameterTypes().get(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMalformedType() throws Exception {
 try {
 new TypeParser(typeFactory).parse("java.util.ArrayList<java.util.List<java.lang.Integer> a>");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownType() throws Exception {
 try {
 new TypeParser(typeFactory).parse("com.unknown.UnknownType");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseClassWithTypeArguments() throws Exception {
 JavaType javaType = new TypeParser(typeFactory).parse("java.util.LinkedHashMap<java.lang.String, java.util.List<java.lang.String>>");
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(LinkedHashMap.class, typeBindings.getRawClass());
 assertEquals(String.class, typeBindings.getKeyType().getRawClass());
 TypeReference valueTypeRef = new TypeReference<List<String>>() {};
 assertEquals(valueTypeRef.getType(), typeBindings.getValueType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingCloseBracket() throws Exception {
 try {
 new TypeParser(typeFactory).parse("java.util.ArrayList<java.util.List<java.lang.String> java.util.ArrayList<java.lang.String>");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}