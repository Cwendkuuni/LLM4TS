package com.example.mapliketype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private JavaType keyType = new SimpleType("java.lang.String");
 private JavaType valueType = new SimpleType("java.lang.Integer");

 @Test
 public void testMapLikeTypeConstructor() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", mapLikeType.toString());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = new TypeReference<List<Map<String, Integer>>>() {}.getType();
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", mapLikeType.toString());
 }

 @Test
 public void testConstruct() {
 try {
 MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 TypeVariable<?>[] typeVariables = Map.class.getTypeParameters();
 MapLikeType mapLikeType = MapLikeType.construct(Map.class, TypeBindings.create(Map.class, keyType, valueType), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", mapLikeType.toString());
 } catch (IllegalArgumentException e) {
 fail("Should not throw exception");
 }
 }

 @Test
 public void testNarrow() {
 JavaType subclass = new TypeReference<HashMap<String, Integer>>() {}.getType();
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType narrowedType = mapLikeType.withTypeHandler(null).narrow(subclass);
 assertEquals("class java.util.HashMap, java.lang.String -> java.lang.Integer", narrowedType.toString());
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = new SimpleType("java.lang.Long");
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals("class java.util.Map, java.lang.Long -> java.lang.Integer", newMapLikeType.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType newValueType = new SimpleType("java.lang.Double");
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Double", newMapLikeType.toString());
 }

 @Test
 public void testWithTypeHandler() {
 Object newValueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newValueHandler);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", newMapLikeType.toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 Object newValueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newValueHandler);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", newMapLikeType.toString());
 }

 @Test
 public void testWithValueHandler() {
 Object newValueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(newValueHandler);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", newMapLikeType.toString());
 }

 @Test
 public void testWithContentValueHandler() {
 Object newValueHandler = new Object() {};
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(newValueHandler);
 assertEquals("class java.util.Map, java.lang.String -> java.lang.Integer", newMapLikeType.toString());
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertTrue(newMapLikeType.isStaticTyping());
 }

 @Test
 public void testRefine() {
 JavaType rawType = new TypeReference<HashMap<String, Integer>>() {}.getType();
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 MapLikeType refinedType = mapLikeType.refine(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null);
 assertEquals("class java.util.HashMap, java.lang.String -> java.lang.Integer", refinedType.toString());
 }

 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertTrue(mapLikeType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertTrue(mapLikeType.isMapLikeType());
 }

 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals(keyType, mapLikeType.getKeyType());
 }

 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals(valueType.getValueHandler(), mapLikeType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertTrue(mapLikeType.hasHandlers());
 }

 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);
 assertTrue(mapLikeType.isTrueMapType());
 }

}