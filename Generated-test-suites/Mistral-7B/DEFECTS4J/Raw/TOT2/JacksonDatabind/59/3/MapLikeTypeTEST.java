package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for default constructor
 @Test
 public void testMapLikeTypeDefaultConstructor() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("java.util.Map", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertNull(mapLikeType.getValueHandler());
 assertNull(mapLikeType.getTypeHandler());
 }

 // Test case for constructor with TypeBindings
 @Test
 public void testMapLikeTypeWithTypeBindingsConstructor() {
 Class<?> rawType = Map.class;
 TypeVariable<?>[] vars = rawType.getTypeParameters();
 TypeBindings bindings = TypeBindings.create(rawType, String.class, Integer.class);
 JavaType keyType = JavaType.constructType(String.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapLikeType mapLikeType = new MapLikeType(rawType, bindings, _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals("java.util.Map<String, Integer>", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for upgradeFrom method
 @Test
 public void testMapLikeTypeUpgradeFrom() {
 JavaType baseType = JavaType.constructType(HashMap.class);
 JavaType keyType = JavaType.constructType(String.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals("java.util.Map<String, Integer>", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test case for withKeyType method
 @Test
 public void testMapLikeTypeWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType newKeyType = JavaType.constructType(Long.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals("java.util.Map<Long, Integer>", newMapLikeType.getClassName());
 assertEquals(Long.class, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 }

 // Test case for isTrueMapType method
 @Test
 public void testMapLikeTypeIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for withTypeHandler method
 @Test
 public void testMapLikeTypeWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertNotNull(newMapLikeType.getTypeHandler());
 assertEquals(handler, newMapLikeType.getTypeHandler());
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testMapLikeTypeWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new Object();
 JavaType contentType = JavaType.constructType(Float.class);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertNotNull(newMapLikeType.getContentTypeHandler());
 assertEquals(handler, newMapLikeType.getContentTypeHandler());
 assertEquals(Float.class, newMapLikeType.getContentType());
 }

 // Test case for withValueHandler method
 @Test
 public void testMapLikeTypeWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertNotNull(newMapLikeType.getValueHandler());
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 // Test case for withContentValueHandler method
 @Test
 public void testMapLikeTypeWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new Object();
 JavaType contentValueType = JavaType.constructType(Double.class);
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
 assertNotNull(newMapLikeType.getContentValueHandler());
 assertEquals(handler, newMapLikeType.getContentValueHandler());
 assertEquals(Double.class, newMapLikeType.getContentType().getRawClass());
 }

 // Test case for upgradeFrom method with illegal argument
 @Test(expected = IllegalArgumentException.class)
 public void testMapLikeTypeUpgradeFromIllegalArgument() {
 JavaType baseType = JavaType.constructType(HashMap.class);
 JavaType keyType = JavaType.constructType(String.class);
 JavaType valueType = JavaType.constructType(Integer.class);
 MapLikeType.upgradeFrom(baseType, keyType, valueType);
 }

 // Test case for isContainerType method
 @Test
 public void testMapLikeTypeIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for isMapLikeType method
 @Test
 public void testMapLikeTypeIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isMapLikeType());
 }
}