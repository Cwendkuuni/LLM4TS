package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final TypeFactory typeFactory = new TypeFactoryBuilder()
         .build();

 @org.junit.BeforeClass
 public static void setup() throws Exception {
 }

 @org.junit.AfterClass
 public static void teardown() throws Exception {
 }

 @org.junit.Test
public void testIdFromValue() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 Object value = new Object();
 String actual = resolver.idFromValue(value);
 String expected = value.getClass().getName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testIdFromValueAndType() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 Object value = new Object();
 Class<?> type = String.class;
 String actual = resolver.idFromValueAndType(value, type);
 String expected = type.getName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testTypeFromId() throws Exception {
 DatabindContext context = new DatabindContext();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 String id = "java.lang.String";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructType(String.class);
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testTypeFromIdWithGenericType() throws Exception {
 DatabindContext context = new DatabindContext();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 String id = "java.util.List<java.lang.String>";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testIdFromValueWithEnum() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 Enum e = Enum.VALUES[0];
 String actual = resolver.idFromValue(e);
 String expected = e.getClass().getName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testIdFromValueWithInternalClass() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 List list = Arrays.asList("one", "two");
 String actual = resolver.idFromValue(list);
 String expected = "java.util.ArrayList";
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testIdFromValueWithInnerClass() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 Outer outer = new Outer();
 Inner inner = outer.new Inner();
 String actual = resolver.idFromValue(inner);
 String expected = "com.fasterxml.jackson.databind.jsontype.impl.Outer$Inner";
 assertEquals(expected, actual);
 }

 @org.junit.Test
public void testTypeFromIdWithInnerClass() throws Exception {
 DatabindContext context = new DatabindContext();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 String id = "com.fasterxml.jackson.databind.jsontype.impl.Outer$Inner";
 JavaType actual = resolver.typeFromId(context, id);
 JavaType expected = typeFactory.constructType(Outer.Inner.class);
 assertEquals(expected, actual);
 }

 private static class Outer {
 static class Inner {
 }
 }
}