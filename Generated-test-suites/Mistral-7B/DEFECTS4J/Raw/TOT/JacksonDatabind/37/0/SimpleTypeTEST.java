package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.MapLikeType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType intSimpleType = new SimpleType(intType);
 SimpleType intSimpleTypeConstruct = SimpleType.construct(intType);
 assertEquals(intSimpleType, intSimpleTypeConstruct);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeConstruction() {
 Class<Map<String, String>> mapType = Map.class;
 SimpleType mapSimpleType = new SimpleType(mapType);
 fail("Should not be able to construct SimpleType for a Map");
 }

 @Test
 public void testTypeNarrowing() {
 Class<Integer> intType = Integer.TYPE;
 Class<Number> numberType = Number.class;
 SimpleType intSimpleType = new SimpleType(intType);
 JavaType narrowedType = intSimpleType.narrow(numberType);
 assertTrue(narrowedType instanceof SimpleType);
 assertSame(Integer.TYPE, narrowedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeNarrowing() {
 Class<Integer> intType = Integer.TYPE;
 Class<String> stringType = String.class;
 SimpleType intSimpleType = new SimpleType(intType);
 JavaType narrowedType = intSimpleType.narrow(stringType);
 fail("Should not be able to narrow type from Integer to String");
 }

 @Test
 public void testTypeWithContentTypeHandler() {
 // Assume we have a SimpleType for String, and a ContentTypeHandler for JSON strings
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 SimpleType stringSimpleType = new SimpleType(stringType.getRawClass(), TypeBindings.emptyBindings(), null, null);
 Object contentTypeHandler = new MyStringContentTypeHandler();
 SimpleType stringSimpleTypeWithContentTypeHandler = stringSimpleType.withContentTypeHandler(contentTypeHandler);
 assertNotNull(stringSimpleTypeWithContentTypeHandler.getContentTypeHandler());
 assertEquals(contentTypeHandler, stringSimpleTypeWithContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testTypeWithTypeHandler() {
 // Assume we have a SimpleType for Integer, and a TypeHandler for reading/writing integers
 JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
 SimpleType intSimpleType = new SimpleType(intType.getRawClass(), TypeBindings.emptyBindings(), null, null);
 Object intTypeHandler = new MyIntTypeHandler();
 SimpleType intSimpleTypeWithTypeHandler = intSimpleType.withTypeHandler(intTypeHandler);
 assertNotNull(intSimpleTypeWithTypeHandler.getTypeHandler());
 assertEquals(intTypeHandler, intSimpleTypeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testTypeWithValueHandler() {
 // Assume we have a SimpleType for String, and a ValueHandler for reading/writing strings
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 SimpleType stringSimpleType = new SimpleType(stringType.getRawClass(), TypeBindings.emptyBindings(), null, null);
 Object stringValueHandler = new MyStringValueHandler();
 SimpleType stringSimpleTypeWithValueHandler = stringSimpleType.withValueHandler(stringValueHandler);
 assertNotNull(stringSimpleTypeWithValueHandler.getValueHandler());
 assertEquals(stringValueHandler, stringSimpleTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testTypeWithStaticTyping() {
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 SimpleType stringSimpleType = new SimpleType(stringType.getRawClass(), TypeBindings.emptyBindings(), null, null);
 SimpleType stringSimpleTypeStatic = stringSimpleType.withStaticTyping();
 assertFalse(stringSimpleTypeStatic.isDynamic());
 assertTrue(stringSimpleTypeStatic.isStatic());
 }

 @Test
 public void testTypeBindings() {
 Class<Map<String, Map<String, String>>> nestedMapType = Map.class;
 MapLikeType mapBindings = TypeBindings.mapLikeTypes().findAndBind("Map<String, Map<String,String>>", nestedMapType);
 Class<String> stringType = String.class;
 JavaType stringTypeInstance = TypeFactory.defaultInstance().constructType(stringType);
 SimpleType stringSimpleType = new SimpleType(stringType.getRawClass(), null, null, null);
 SimpleType nestedMapSimpleType = new SimpleType(nestedMapType, mapBindings, null, null);
 SimpleType mapSimpleType = new SimpleType(Map.class, mapBindings, null, null);
 SimpleType mapSimpleTypeWithStringKey = new SimpleType(Map.class, mapBindings, stringTypeInstance, null);
 SimpleType mapSimpleTypeWithNestedMapValue = new SimpleType(Map.class, mapBindings, nestedMapType, null);
 SimpleType mapSimpleTypeWithBoth = new SimpleType(Map.class, mapBindings, stringTypeInstance, nestedMapType);

 assertEquals(stringTypeInstance, mapSimpleTypeWithStringKey.getContentType());
 assertEquals(nestedMapType, mapSimpleTypeWithNestedMapValue.getContentType());
 assertNull(mapSimpleType.getContentType());
 assertTrue(mapSimpleTypeWithBoth.getContentTypeHandler() instanceof EmptyContentTypeHandler);
 assertFalse(mapSimpleTypeWithBoth.hasContentTypeHandler());
 }

 @Test
 public void testTypeFactoryConstruction() {
 Class<Integer> intType = Integer.TYPE;
 JavaType intTypeInstance = TypeFactory.defaultInstance().constructType(intType);
 SimpleType intSimpleType = (SimpleType) intTypeInstance;
 SimpleType intSimpleTypeConstruct = new SimpleType(intSimpleType);
 assertEquals(intSimpleType, intSimpleTypeConstruct);
 }

 @Test
 public void testUnsafeTypeConstruction() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType intSimpleTypeUnsafe = SimpleType.constructUnsafe(intType);
 SimpleType intSimpleType = new SimpleType(intType);
 assertEquals(intSimpleType, intSimpleTypeUnsafe);
 }
}