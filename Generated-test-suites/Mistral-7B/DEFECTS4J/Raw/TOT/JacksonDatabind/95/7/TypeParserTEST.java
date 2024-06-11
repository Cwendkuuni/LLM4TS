package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 _factory = new TypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParseSimpleClass() {
 String canonical = "int";
 JavaType type = _parser.parse(canonical);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonical = "java.util.ArrayList<java.lang.Integer>";
 JavaType type = _parser.parse(canonical);
 TypeReference<List<Integer>> ref = new TypeReference<List<Integer>>() {};
 assertEquals(ref.getType(), type);
 }

 @Test
 public void testParseClassWithComma() {
 String canonical = "int, boolean";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithMissingEndToken() {
 String canonical = "java.util.ArrayList<java.lang.Integer<";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidToken() {
 String canonical = "java.util.ArrayList<java.lang.Integer<abc";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidClassName() {
 String canonical = "java.util.ArrayList<java.lang.IntegerAbc";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithEmptyInput() {
 String canonical = "";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidInput() {
 String canonical = "foo";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithNestedGenerics() {
 String canonical = "java.util.ArrayList<java.util.ArrayList<java.lang.Integer>>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArray() {
 String canonical = "int[]";
 JavaType type = _parser.parse(canonical);
 assertEquals(int[].class, type.getRawClass());
 }

 @Test
 public void testParseClassWithArrayWithGenerics() {
 String canonical = "java.util.ArrayList<int[]>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test public void testParseClassWithArrayWithNestedGenerics() {
 String canonical = "java.util.ArrayList<java.util.ArrayList<int[]>>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidArrayInput() {
 String canonical = "int[]foo";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithInvalidToken() {
 String canonical = "int[]<foo>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithEmptyInput() {
 String canonical = "int[]";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithInvalidInput() {
 String canonical = "int[]foo";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidArrayInputLength() {
 String canonical = "int[][";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithInvalidArrayInputLength2() {
 String canonical = "int[][]";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithMissingEndToken() {
 String canonical = "int[]<";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithMultipleGenerics() {
 String canonical = "java.util.List<java.util.List<java.lang.Integer[], java.lang.Long[]>>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }

 @Test
 public void testParseClassWithArrayWithMultipleGenerics2() {
 String canonical = "java.util.List<java.util.List<java.lang.Integer[], java.lang.Long[]>, java.lang.String>";
 JavaType type = null;
 try {
 type = _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(type);
 }
}