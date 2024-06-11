package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.*;

public class ClassNameIdResolverTest {

 private static final TypeFactory typeFactory = new TypeFactoryBuilder()
         .build();
 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
 assertNotNull(resolver);
 }

 @Test
 public void testGetMechanism() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);
 assertEquals(Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 // Not applicable for this test case
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = new ClassNameIdResolver(String.class, typeFactory).idFromValue(value);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 String typeName = String.class.getName();
 String id = new ClassNameIdResolver(String.class, typeFactory)
         .idFromValueAndType(value, Class.forName(typeName));
 assertEquals(typeName, id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 DatabindContext context = mapper.getDeserializationContext();
 String id = "java.lang.String";
 JavaType javaType = new ClassNameIdResolver(String.class, typeFactory)
         .typeFromId(context, id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testIdFrom_Enum() throws Exception {
 Enum e = Enum.valueOf(String.class, "Test");
 String id = new ClassNameIdResolver(String.class, typeFactory)
         ._idFrom(e, e.getClass(), typeFactory);
 assertEquals("java.lang.String", id);
 }

 @Test
 public void testIdFrom_EnumSubclass() throws Exception {
 Enum subEnum = Enum.valueOf(StringSubclass.class, "TestSubclass");
 String id = new ClassNameIdResolver(String.class, typeFactory)
         ._idFrom(subEnum, subEnum.getClass(), typeFactory);
 assertEquals("com.yourpackage.test.StringSubclass", id);
 }

 @Test
 public void testIdFrom_Array() throws Exception {
 Object array = new Object[5];
 String id = new ClassNameIdResolver(String.class, typeFactory)
         ._idFrom(array, array.getClass(), typeFactory);
 assertEquals("java.lang.Object[]", id);
 }

 @Test
 public void testIdFrom_ArrayOfEnum() throws Exception {
 Object array = new Enum[5];
 String id = new ClassNameIdResolver(String.class, typeFactory)
         ._idFrom(array, array.getClass(), typeFactory);
 assertEquals("java.lang.Enum[]", id);
 }

 @Test
 public void testIdFrom_Collection() throws Exception {
 List list = Arrays.asList("one", "two");
 String id = new ClassNameIdResolver(List.class, typeFactory)
         ._idFrom(list, list.getClass(), typeFactory);
 assertEquals("java.util.ArrayList", id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFrom_InvalidId() throws Exception {
 String id = new ClassNameIdResolver(String.class, typeFactory)
         ._idFrom(new Object(), String.class, typeFactory);
 fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testTypeFromId_UnknownClass() throws Exception {
 DatabindContext context = mapper.getDeserializationContext();
 String id = "com.unknown.UnknownClass";
 JavaType javaType = new ClassNameIdResolver(String.class, typeFactory)
         .typeFromId(context, id);
 fail("Expected ClassNotFoundException but got none.");
 }
}