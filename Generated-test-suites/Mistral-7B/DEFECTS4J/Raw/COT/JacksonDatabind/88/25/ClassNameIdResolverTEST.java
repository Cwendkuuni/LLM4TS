package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

 private TypeFactory typeFactory;
 private DatabindContext context;
 private ClassNameIdResolver resolver;

 @Before
 public void setup() {
 typeFactory = new TypeFactoryBuilder().build();
 context = new TestBase();
 resolver = new ClassNameIdResolver(Object.class, typeFactory);
 }

 @Test
 public void testGetMechanism() {
 JsonTypeInfo.Id actual = resolver.getMechanism();
 assertEquals("Id.CLASS", actual.getId());
 }

 @Test
 public void testRegisterSubtype() {
 try {
 resolver.registerSubtype(String.class, "StringType");
 fail("Method should not be called with ClassNameIdResolver");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIdFromValue() {
 Object value = new Object();
 String actual = resolver.idFromValue(value);
 String expected = value.getClass().getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromValueAndType() {
 Object value = new Object();
 Class<?> type = String.class;
 String actual = resolver.idFromValueAndType(value, type);
 String expected = type.getName();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromValueAndInvalidType() {
 Object value = new Object();
 Class<?> invalidType = new Object() {}.getClass(); // anonymous class
 String actual = resolver.idFromValueAndType(value, invalidType);
 fail("Should not reach this point");
 }

 @Test
 public void testTypeFromIdWithBasicClassName() throws IOException {
 String id = "java.lang.String";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructType(String.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithClassObject() throws IOException {
 String id = "java.lang.Class";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructType(Class.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithArrayClass() throws IOException {
 String id = "java.lang.int[]";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructArrayType(Integer.TYPE);
 assertEquals(expected, actual);
 }

 @Test
 public void testTypeFromIdWithParameterizedType() throws IOException {
 String id = "java.util.HashMap<java.lang.String, java.lang.Integer>";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructMapType(HashMap.class, String.class, Integer.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromWithEnumClass() {
 Object value = Enum.values()[0];
 String actual = resolver.idFromValue(value);
 // Enum constants have no class, so we will check the name of the enum class
 String expected = value.getClass().getName();
 expected = expected.substring(0, expected.lastIndexOf(".")) + ".class";
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromWithEnumSubclass() {
 Enum parentEnum = Enum.class.getEnumConstants()[0];
 Enum childEnum = new Enum() {
 @Override public String name() { return "ChildEnum"; }
 }.getClass();
 Object value = childEnum.valueOf("ChildEnum");
 String actual = resolver.idFromValue(value);
 String expected = parentEnum.getClass().getName() + "$ChildEnum";
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromWithInnerClass() {
 Object value = new TestOuter().TestInner();
 String actual = resolver.idFromValue(value);
 String expected = TestOuter.class.getName() + "$TestInner";
 assertEquals(expected, actual);
 }

 @Test
 public void testIdFromWithAnonymousInnerClass() {
 Object value = new Object() {
 public void foo() {}
 }.new AnonymousInnerClass();
 String actual = resolver.idFromValue(value);
 // We cannot easily construct the expected type, but we can check that
 // the generated id starts with the enclosing class name
 String expected = value.getClass().getEnclosingClass().getName() + "$AnonymousInnerClass";
 assertTrue(actual.startsWith(expected));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromWithInvalidValue() {
 Object value = new Object();
 String actual = resolver.idFromValue(null);
 fail("Should not reach this point");
 }

 @Test
 public void testIdFromWithNullValueAndType() {
 Object value = null;
 Class<?> type = String.class;
 String actual = resolver.idFromValueAndType(value, type);
 fail("Should not reach this point");
 }
}