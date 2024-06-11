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

 private TypeFactory _factory;
 private TypeParser _parser;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 _factory = new TypeFactoryBuilder().build();
 }

 @Before
 public void setUp() {
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParse() throws Exception {
 String canonical = "java.lang.String";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedTokens() throws Exception {
 String canonical = "java.lang.String unexpectedToken";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 String canonical = "java.lang.String unexpectedToken<";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClass() throws Exception {
 String canonical = "java.lang.String.invalidClass";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testParseClassWithGenerics() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertEquals(expected.getTypeParams(), actual.getTypeParams());
 }

 @Test
 public void testParseClassWithMultipleGenerics() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertEquals(expected.getTypeParams(), actual.getTypeParams());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidGenericDefinition() throws Exception {
 String canonical = "java.util.List<java.lang.String<java.lang.Integer>>";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testParseClassWithArray() throws Exception {
 String canonical = "[java.lang.String]";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertArrayEquals(expected.getComponentType().getRawClass().getComponentType(), actual.getComponentType().getRawClass());
 }

 @Test
 public void testParseClassWithMultipleArrays() throws Exception {
 String canonical = "[[java.lang.String]]";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertArrayEquals(expected.getComponentType().getRawClass().getComponentType(), actual.getComponentType().getRawClass());
 }

 @Test
 public void testParseClassWithEmptyArray() throws Exception {
 String canonical = "[]";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertEquals(expected.getComponentType(), actual.getComponentType());
 assertNull(expected.getTypeParams());
 assertNull(actual.getTypeParams());
 }

 @Test
 public void testParseClassWithArrayAndGenerics() throws Exception {
 String canonical = "[java.util.List<java.lang.String>]";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertArrayEquals(expected.getComponentType().getRawClass(), actual.getComponentType().getRawClass());
 assertEquals(expected.getTypeParams(), actual.getTypeParams());
 }

 @Test
 public void testParseClassWithEmptyList() throws Exception {
 String canonical = "<java.util.List<>>";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertEquals(expected.getTypeParams(), actual.getTypeParams());
 assertNotNull(expected.getComponentType());
 assertNull(actual.getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEmptyList() throws Exception {
 String canonical = "<java.util.List>>";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 _parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testParseClassWithComplexType() throws Exception {
 String canonical = "java.util.Map<java.util.List<java.lang.String>, java.util.List<java.lang.Integer>>";
 JavaType expected = _factory.constructType(canonical);
 JavaType actual = _parser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 assertEquals(expected.getTypeParams(), actual.getTypeParams());
 assertNotNull(expected.getComponentType());
 assertNotNull(actual.getComponentType());
 assertNotNull(expected.getTypeParams().get(0).getComponentType());
 assertNotNull(actual.getTypeParams().get(0).getComponentType());
 assertNotNull(expected.getTypeParams().get(1).getComponentType());
 assertNotNull(actual.getTypeParams().get(1).getComponentType());
 }

 @Test
 public void testParseClassWithNullInput() throws Exception {
 String canonical = null;
 try {
 _parser.parse(canonical);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 private void assertArrayEquals(Class<?> expected, Class<?> actual) {
 List<Class<?>> expectedComponents = new ArrayList<>();
 if (expected != null) {
 expectedComponents.add(expected.getComponentType());
 while (expected.getComponentType() != null) {
 expected = expected.getComponentType();
 expectedComponents.add(expected);
 }
 }
 List<Class<?>> actualComponents = new ArrayList<>();
 if (actual != null) {
 actualComponents.add(actual.getComponentType());
 while (actual.getComponentType() != null) {
 actual = actual.getComponentType();
 actualComponents.add(actual);
 }
 }
 assertEquals("Unexpected array components", expectedComponents, actualComponents);
 }

}