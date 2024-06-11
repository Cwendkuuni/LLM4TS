package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 assertEquals(intClass, intType.getRawClass());
 assertNull(intType.getSuperClass());
 assertNull(intType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeConstructionWithSuperClass() {
 Class<Integer> intClass = Integer.class;
 Class<Number> numberClass = Number.class;
 SimpleType intType = new SimpleType(intClass, null, numberClass, null);
 assertEquals(intClass, intType.getRawClass());
 assertEquals(numberClass, intType.getSuperClass());
 assertNull(intType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeConstructionWithSuperInterfaces() {
 Class<Integer> intClass = Integer.class;
 JavaType arrayListJavaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
 SimpleType intType = new SimpleType(intClass, null, null, new JavaType[] { arrayListJavaType });
 assertEquals(intClass, intType.getRawClass());
 assertNull(intType.getSuperClass());
 assertArrayEquals(new JavaType[] { arrayListJavaType }, intType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 SimpleType intTypeCopy = new SimpleType(intType);
 assertEquals(intType, intTypeCopy);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 Object intValueHandler = new Object() {};
 SimpleType intTypeWithValueHandler = new SimpleType(intType, null, null, null, intValueHandler, null, false);
 assertNotSame(intType, intTypeWithValueHandler);
 assertSame(intType.getRawClass(), intTypeWithValueHandler.getRawClass());
 assertEquals(intValueHandler, intTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 Object intTypeHandler = new Object() {};
 SimpleType intTypeWithTypeHandler = new SimpleType(intType, null, null, null, null, intTypeHandler, false);
 assertNotSame(intType, intTypeWithTypeHandler);
 assertSame(intType.getRawClass(), intTypeWithTypeHandler.getRawClass());
 assertEquals(intTypeHandler, intTypeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 SimpleType intTypeStatic = intType.withStaticTyping();
 assertNotSame(intType, intTypeStatic);
 assertTrue(intTypeStatic.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithMap() {
 Class<Map<Integer,String>> mapClass = new Class<Map<Integer,String>>() {};
 SimpleType mapType = new SimpleType(mapClass);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithCollection() {
 Class<List<Integer>> listClass = new Class<List<Integer>>() {};
 SimpleType listType = new SimpleType(listClass);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithArray() {
 Class<int[]> intArrayClass = int[].class;
 SimpleType intArrayType = new SimpleType(intArrayClass);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeWithMap() {
 Class<Map<Integer,String>> mapClass = new Class<Map<Integer,String>>() {};
 SimpleType mapType = SimpleType.constructUnsafe(mapClass);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeWithCollection() {
 Class<List<Integer>> listClass = new Class<List<Integer>>() {};
 SimpleType listType = SimpleType.constructUnsafe(listClass);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeWithArray() {
 Class<int[]> intArrayClass = int[].class;
 SimpleType intArrayType = SimpleType.constructUnsafe(intArrayClass);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeWithContentType() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 JavaType intArrayJavaType = TypeFactory.defaultInstance().constructType(int[].class);
 SimpleType intTypeWithContentType = intType.withContentType(intArrayJavaType);
 assertNotSame(intType, intTypeWithContentType);
 assertSame(intType.getRawClass(), intTypeWithContentType.getRawClass());
 assertNotNull(intTypeWithContentType.getSuperClass());
 assertArrayEquals(new JavaType[] { intArrayJavaType }, intTypeWithContentType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithTypeHandlerAndContentTypeHandler() {
 SimpleType intType = new SimpleType(Integer.TYPE);
 Object intTypeHandler = new Object() {};
 Object intContentTypeHandler = new Object() {};
 SimpleType intTypeWithTypeHandlerAndContentTypeHandler = intType.withTypeHandler(intTypeHandler).withContentTypeHandler(intContentTypeHandler);
 assertNotSame(intType, intTypeWithTypeHandlerAndContentTypeHandler);
 assertSame(intType.getRawClass(), intTypeWithTypeHandlerAndContentTypeHandler.getRawClass());
 assertEquals(intTypeHandler, intTypeWithTypeHandlerAndContentTypeHandler.getTypeHandler());
 assertEquals(intContentTypeHandler, intTypeWithTypeHandlerAndContentTypeHandler.getContentHandler());
 }
}