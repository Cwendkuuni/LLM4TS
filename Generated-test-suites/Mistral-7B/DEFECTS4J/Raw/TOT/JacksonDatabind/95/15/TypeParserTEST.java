package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory(); // assuming TypeFactory is accessible
 private TypeParser typeParser;

 @Test
 public void testParseSimpleType() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 JavaType expectedType = typeFactory.constructType(String.class);
 JavaType actualType = testee.parse("String");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseClassWithGenerics() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 JavaType expectedType = typeFactory.constructType(ArrayList.class, Integer.class);
 JavaType actualType = testee.parse("java.util.ArrayList<java.lang.Integer>");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseInvalidTokenSequence() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("java.util.ArrayList<java.lang.Integer>X"); // X is an invalid token
 try {
 JavaType actualType = testee.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'X', expected ',' or '>')";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseClassNotFound() throws IllegalArgumentException, ClassNotFoundException {
 TypeParser testee = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("java.lang.MyClass"); // MyClass does not exist
 try {
 JavaType actualType = testee.parseType(tokens);
 fail("Expected IllegalStateException or ClassNotFoundException");
 } catch (RuntimeException e) {
 if (e.getCause() instanceof ClassNotFoundException) {
 // expected
 } else {
 fail("Unexpected exception: " + e.getClass().getName());
 }
 }
 }

 @Test
 public void testParseRuntimeException() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("java.lang.MyClass<RuntimeException>"); // MyClass does not exist
 try {
 JavaType actualType = testee.parseType(tokens);
 fail("Expected IllegalArgumentException or RuntimeException");
 } catch (IllegalArgumentException e) {
 // expected
 } catch (RuntimeException e) {
 if (!(e instanceof RuntimeException)) {
 fail("Unexpected exception: " + e.getClass().getName());
 }
 }
 }

 @Test
 public void testParseInvalidGenericArguments() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("java.util.ArrayList<java.lang.String>java.lang.Integer>");
 try {
 JavaType actualType = testee.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token 'java.lang.Integer>', expected ',' or '>')";
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }

 @Test
 public void testParseEmptyType() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 JavaType expectedType = typeFactory.constructType(Void.TYPE);
 JavaType actualType = testee.parse("Void");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testParseInvalidTypeName() throws IllegalArgumentException {
 TypeParser testee = new TypeParser(typeFactory);
 MyTokenizer tokens = new MyTokenizer("java.util.ArrayList<java.lang.InvalidType>"); // InvalidType is not a valid type
 try {
 JavaType actualType = testee.parseType(tokens);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'java.lang.InvalidType'";
 String actualMessage = e.getCause().getMessage();
 assertTrue(actualMessage.contains(expectedMessage));
 }
 }
}