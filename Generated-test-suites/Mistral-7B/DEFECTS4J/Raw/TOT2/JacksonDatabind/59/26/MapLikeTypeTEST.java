package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private MapLikeType _mapType;
 private JavaType _stringJavaType;
 private JavaType _integerJavaType;
 private JavaType _mapTypeWithStringInteger;

 @Before
 public void setUp() {
 _stringJavaType = JavaType.construct(String.class, null);
 _integerJavaType = JavaType.construct(int.class, null);

 _mapType = MapLikeType.construct(Map.class, _stringJavaType, _integerJavaType);
 _mapTypeWithStringInteger = MapLikeType.upgradeFrom(_mapType.getRawClass(), _stringJavaType, _integerJavaType);
 }

 @Test
 public void testBasicMapLikeTypeCreation() {
 MapLikeType mapLikeType = new MapLikeType(_mapType.getRawClass(), _bindings, _bogusSuperClass(_mapType.getRawClass()), null, _stringJavaType, _integerJavaType);
 assertEquals(_mapType, mapLikeType);
 }

 @Test
 public void testUpgradeFromJavaType() {
 MapLikeType upgradedMapType = MapLikeType.upgradeFrom(_mapType, _stringJavaType, _integerJavaType);
 assertEquals(_mapType, upgradedMapType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotUpgradeFromNonTypeBase() {
 MapLikeType.upgradeFrom(String.class, _stringJavaType, _integerJavaType);
 }

 @Test
 public void testGetKeyType() {
 JavaType keyType = _mapType.getKeyType();
 assertEquals(_stringJavaType, keyType);
 }

 @Test
 public void testGetContentType() {
 JavaType contentType = _mapType.getContentType();
 assertEquals(_integerJavaType, contentType);
 }

 @Test
 public void testIsContainerType() {
 assertTrue(_mapType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 assertTrue(_mapType.isMapLikeType());
 }

 @Test
 public void testIsTrueMapType() {
 assertTrue(_mapType.isTrueMapType());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 _mapType.getErasedSignature(sb);
 assertEquals("java.util.Map", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 _mapType.getGenericSignature(sb);
 assertEquals("[map-like type; class java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]", sb.toString());
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = JavaType.construct(Integer.class, null);
 MapLikeType newMapType = _mapType.withKeyType(newKeyType);
 assertNotSame(_mapType, newMapType);
 assertEquals(newKeyType, newMapType.getKeyType());
 }

 @Test
 public void testWithContentType() {
 JavaType newContentType = JavaType.construct(Double.class, null);
 MapLikeType newMapType = _mapType.withContentType(newContentType);
 assertNotSame(_mapType, newMapType);
 assertEquals(newContentType, newMapType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<String, Integer>().put("test", 1);
 }
 };
 MapLikeType newMapType = _mapType.withTypeHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithContentTypeHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<String, Integer>().put("test", 1);
 }
 };
 MapLikeType newMapType = _mapType.withContentTypeHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithValueHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<String, Integer>().put("test", 1);
 }
 };
 MapLikeType newMapType = _mapType.withValueHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithContentValueHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<String, Integer>().put("test", 1);
 }
 };
 MapLikeType newMapType = _mapType.withContentValueHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithKeyTypeHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<Integer, Integer>().put(1, 1);
 }
 };
 MapLikeType newMapType = _mapType.withKeyTypeHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithKeyValueHandler() {
 TypeHandler handler = new TypeHandlerBase() {
 @Override
 public Object deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new HashMap<Integer, Integer>().put(1, 1);
 }
 };
 MapLikeType newMapType = _mapType.withKeyValueHandler(handler);
 assertNotSame(_mapType, newMapType);
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType newMapType = _mapType.withStaticTyping();
 assertNotSame(_mapType, newMapType);
 assertTrue(newMapType.isStaticTyping());
 }

 @Test
 public void testRefine() {
 TypeBindings bindings = TypeBindings.create(Map.class, _stringJavaType, _integerJavaType);
 JavaType refinedMapType = _mapType.refine(Map.class, bindings, null, null);
 assertEquals(_mapTypeWithStringInteger, refinedMapType);
 }

 @Test
 public void testToString() {
 assertEquals("[map-like type; class java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]", _mapType.toString());
 }
}