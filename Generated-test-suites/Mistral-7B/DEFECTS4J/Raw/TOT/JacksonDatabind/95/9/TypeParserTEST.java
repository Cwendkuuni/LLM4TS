package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeParserTest {

 private TypeFactory typeFactory = new DefaultTypeFactory();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonical = "java.lang.String";
 JavaType expectedType = typeFactory.constructType(canonical);
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseComplexType() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expectedType = new TypeReference<List<Integer>>() {}.getType();
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType.getTypeId(), actualType.getTypeId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.lang.String<Integer>";
 String msg = "Failed to parse type 'java.lang.String<Integer>' (remaining: '>': Unexpected token '>'";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 assertEquals(msg, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyType() {
 String canonical = "";
 String msg = "Failed to parse type '' (remaining: '': Unexpected end-of-string)";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 assertEquals(msg, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeWithInvalidToken() {
 String canonical = "java.lang.String<FooBar";
 String msg = "Failed to parse type 'java.lang.String<FooBar' (remaining: '>': Unexpected token 'FooBar'";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 assertEquals(msg, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeWithMissingClosingBracket() {
 String canonical = "java.util.List<java.lang.Integer<";
 String msg = "Failed to parse type 'java.util.List<java.lang.Integer<' (remaining: '>': Unexpected end-of-string)";
 try {
 typeParser.parse(canonical);
 } catch (IllegalArgumentException e) {
 assertEquals(msg, e.getMessage());
 }
 }

 @Test
 public void testParseTypeWithNestedGenerics() {
 String canonical = "java.util.List<java.util.List<java.lang.Integer>>";
 JavaType expectedType = new TypeReference<List<List<Integer>>>() {}.getType();
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType.getTypeId(), actualType.getTypeId());
 }

 @Test
 public void testParseTypeWithCustomClass() {
 String className = "com.example.MyCustomClass";
 String canonical = className + "<java.lang.Integer>";
 JavaType expectedType = typeFactory.constructType(className + ":Ljava/lang/Object;<T:Ljava/lang/Integer;>");
 JavaType actualType = typeParser.parse(canonical);
 assertEquals(expectedType.getTypeId(), actualType.getTypeId());
 }
}