package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactoryBuilder().build();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalForm = "int";
 JavaType javaType = typeParser.parse(canonicalForm);
 assertEquals(Integer.TYPE, javaType.getType());
 }

 @Test
 public void testParseClassType() {
 String canonicalForm = "java.lang.String";
 JavaType javaType = typeParser.parse(canonicalForm);
 assertEquals(String.class, javaType.getType());
 }

 @Test
 public void testParseGenericType() {
 String canonicalForm = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonicalForm);
 List<String> list = (List<String>) javaType.getValue();
 assertEquals(String.class, list.getClass().getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalForm = "java.util.List<java.lang.String<Integer>";
 typeParser.parse(canonicalForm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonicalForm = "int<";
 typeParser.parse(canonicalForm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingType() {
 String canonicalForm = "java.util.List";
 typeParser.parse(canonicalForm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 String canonicalForm = "java.util.List<java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonicalForm);
 typeParser.parseType(tokens);
 tokens.nextToken(); // consume '>'
 tokens.pushBack(">"); // pushback '>'
 typeParser.parseType(tokens); // should throw exception
 }

 @Test
 public void testParseListOfList() {
 String canonicalForm = "java.util.List<java.util.List<java.lang.String>>";
 JavaType javaType = typeParser.parse(canonicalForm);
 List<List<String>> listOfList = (List<List<String>>) javaType.getValue();
 List<String> innerList = listOfList.get(0);
 assertEquals(String.class, innerList.getClass().getComponentType());
 }

 @Test
 public void testParseTypeWithFactory() {
 TypeFactory newTypeFactory = new TypeFactoryBuilder().build();
 TypeParser newTypeParser = new TypeParser(newTypeFactory);
 JavaType javaType = newTypeParser.parse("java.util.List<java.lang.String>");
 assertEquals(List.class, javaType.getType());

 TypeParser oldTypeParser = this.typeParser;
 JavaType oldJavaType = oldTypeParser.parse("java.util.List<java.lang.String>");
 assertEquals(List.class, oldJavaType.getType());

 // test withFactory method
 TypeParser withFactoryParser = oldTypeParser.withFactory(newTypeFactory);
 JavaType withFactoryJavaType = withFactoryParser.parse("java.util.List<java.lang.String>");
 assertEquals(List.class, withFactoryJavaType.getType());
 }
}