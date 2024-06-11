package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * JUnit 4 test cases for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

 private TypeFactory typeFactory;
 private JavaType baseType;
 private DatabindContext context;
 private ClassNameIdResolver resolver;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 typeFactory = new TypeFactoryBuilder()
     .build();
 baseType = typeFactory.constructType(Object.class);
 context = new ObjectMapper().getDeserializationContext();
 resolver = new ClassNameIdResolver(baseType, typeFactory);
 }

 /**
  * Test for {@link ClassNameIdResolver#ClassNameIdResolver(JavaType, TypeFactory)} constructor.
  */
 @Test
 public void testClassNameIdResolverConstructor() {
 // Check constructor argument types
 assertNotNull(baseType);
 assertNotNull(typeFactory);
 }

 /**
  * Test for {@link ClassNameIdResolver#getMechanism()} method.
  */
 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 /**
  * Test for {@link ClassNameIdResolver#registerSubtype(Class, String)} method.
  * This method is not used with class name - based resolvers.
  */
 @Test(expected = UnsupportedOperationException.class)
 public void testRegisterSubtype() {
 resolver.registerSubtype(String.class, "StringType");
 }

 /**
  * Test for {@link ClassNameIdResolver#idFromValue(Object)} method.
  */
 @Test
 public void testIdFromValue() {
 Object value = new Object();
 String expectedId = value.getClass().getName();
 String actualId = resolver.idFromValue(value);
 assertEquals(expectedId, actualId);
 }

 /**
  * Test for {@link ClassNameIdResolver#idFromValueAndType(Object, Class)} method.
  */
 @Test
 public void testIdFromValueAndType() {
 Object value = new Object();
 Class<?> type = Object.class;
 String expectedId = type.getName();
 String actualId = resolver.idFromValueAndType(value, type);
 assertEquals(expectedId, actualId);
 }

 /**
  * Test for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)} method.
  */
 @Test
 public void testTypeFromId() throws IOException {
 String id = String.class.getName();
 JavaType expectedType = typeFactory.constructType(String.class);
 JavaType actualType = resolver.typeFromId(context, id);
 assertEquals(expectedType, actualType);
 }

 /**
  * Test for corner case when id is null.
  */
 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdNullId() throws IOException {
 String id = null;
 resolver.typeFromId(context, id);
 }

 /**
  * Test for corner case when id is an empty string.
  */
 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdEmptyId() throws IOException {
 String id = "";
 resolver.typeFromId(context, id);
 }

 /**
  * Test for corner case when id is an invalid type id.
  */
 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdInvalidId() throws IOException {
 String id = "InvalidTypeId";
 resolver.typeFromId(context, id);
 }

 /**
  * Test for corner case when id is a generic type.
  */
 @Test
 public void testTypeFromIdGenericType() throws IOException {
 String id = List.class.getName() + "<String>";
 JavaType expectedType = typeFactory.constructType(List.class, String.class);
 JavaType actualType = resolver.typeFromId(context, id);
 assertEquals(expectedType, actualType);
 }

 /**
  * Test for corner case when id is a java.lang.Class object.
  */
 @Test
 public void testTypeFromIdClassObject() throws IOException {
 Class<?> idType = String.class;
 JavaType expectedType = typeFactory.constructType(String.class);
 JavaType actualType = resolver.typeFromId(context, idType);
 assertEquals(expectedType, actualType);
 }

 /**
  * Test for {@link ClassNameIdResolver#_idFrom(Object, Class, TypeFactory)} method.
  * Private method, but can be tested indirectly by testing {@link ClassNameIdResolver#idFromValue(Object)}
  * and {@link ClassNameIdResolver#idFromValueAndType(Object, Class)} methods.
  */
 @Test
 public void testIdFrom() throws Exception {
 Object value = new Object();
 Class<?> cls = value.getClass();
 String expectedId = cls.getName();
 String actualId = resolver._idFrom(value, cls, typeFactory);
 assertEquals(expectedId, actualId);
 }
}