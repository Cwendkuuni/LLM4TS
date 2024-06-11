package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

/**
 * Test cases for TypeParser class
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
 JavaType parsedType = typeParser.parse(canonicalType);
 // check the parsed type is as expected
 Class<?> rawClass = parsedType.getRawClass();
 List<JavaType> typeArguments = parsedType.getTypeParameters();
 assertEquals("java.util.List", rawClass.getName());
 assertEquals(1, typeArguments.size());
 JavaType integerType = typeArguments.get(0);
 assertEquals("java.lang.Integer", integerType.getRawClass().getName());
 }

 @Test
 public void testParse_emptyType() {
 String canonicalType = "java.util.List<>";
 JavaType parsedType = typeParser.parse(canonicalType);
 // check that an empty type returns null
 assertNull(parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType_missingEnd() {
 String canonicalType = "java.util.List<java.lang.Integer";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType_missingLeftBracket() {
 String canonicalType = "java.util.List java.lang.Integer";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType_invalidToken() {
 String canonicalType = "java.util.List<java.lang.Integer>asdf";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidType_missingRightBracket() {
 String canonicalType = "java.util.List<java.lang.Integer<";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidClass() {
 String canonicalType = "java.util.List<java.nonExistentClass>";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParse_validTypeWithWildcard() {
 String canonicalType = "java.util.List<?>";
 JavaType parsedType = typeParser.parse(canonicalType);
 // check that a wildcard type returns a raw type
 Class<?> rawClass = parsedType.getRawClass();
 assertEquals("java.util.List", rawClass.getName());
 }

 @Test
 public void testParse_emptyList() {
 String canonicalType = "java.util.List<>";
 JavaType parsedType = typeParser.parse(canonicalType);
 // check that an empty list type returns an empty list type
 List<JavaType> typeArguments = parsedType.getTypeParameters();
 assertEquals(0, typeArguments.size());
 }

 @Test
 public void testParseTypes_validTypes() {
 String canonicalType = "java.util.List<java.lang.Integer>,java.util.Set<java.lang.String>";
 List<JavaType> parsedTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));
 // check that multiple types are parsed correctly
 assertEquals(2, parsedTypes.size());
 JavaType integerListType = parsedTypes.get(0);
 JavaType stringSetType = parsedTypes.get(1);
 assertEquals("java.util.List", integerListType.getRawClass().getName());
 assertEquals("java.lang.Integer", integerListType.getTypeParameters().get(0).getRawClass().getName());
 assertEquals("java.util.Set", stringSetType.getRawClass().getName());
 assertEquals("java.lang.String", stringSetType.getTypeParameters().get(0).getRawClass().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_invalidToken() {
 String canonicalType = "java.util.List<java.lang.Integer>asdf,java.util.Set<java.lang.String>";
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_missingRightBracket() {
 String canonicalType = "java.util.List<java.lang.Integer>,java.util.Set<java.lang.String<";
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 }

 @Test
 public void testFindClass_validClassName() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType parsedType = new TypeParser(typeFactory).parseType(new MyTokenizer(canonicalType));
 Class<?> foundClass = typeParser.findClass(parsedType.getTypeName().getRawClass().getName(), null);
 assertEquals("java.util.List", foundClass.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_invalidClassName() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType parsedType = new TypeParser(typeFactory).parseType(new MyTokenizer(canonicalType));
 String invalidClassName = "java.nonExistentClass";
 typeParser.findClass(invalidClassName, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProblem_invalidToken() {
 MyTokenizer tokens = new MyTokenizer("InvalidType{invalidToken}");
 typeParser._problem(tokens, "Invalid message");
 }

}