package com.yourpackage.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private JavaType keyType = JavaType.construct(int.class, int.class);
 private JavaType valueType = JavaType.construct(String.class);
 private JavaType mapType = MapLikeType.construct(Map.class, keyType, valueType);

 @Test
 public void testConstruct() {
 MapLikeType actual = MapLikeType.construct(Map.class, keyType, valueType);
 MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 assertEquals(expected, actual);
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.construct(Map.class, int.class, int.class);
 JavaType keyT = JavaType.construct(String.class);
 JavaType valueT = JavaType.construct(String.class);
 MapLikeType actual = MapLikeType.upgradeFrom(baseType, keyT, valueT);
 MapLikeType expected = new MapLikeType(baseType, keyT, valueT);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsContainerType() {
 assertTrue(mapType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 assertTrue(mapType.isMapLikeType());
 }

 @Test
 public void testGetKeyType() {
 assertEquals(keyType, mapType.getKeyType());
 }

 @Test
 public void testGetContentType() {
 assertEquals(valueType, mapType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 assertNull(mapType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 assertFalse(mapType.hasHandlers());
 }

 @Test
 public void testIsTrueMapType() {
 assertTrue(mapType.isTrueMapType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType baseType = JavaType.construct(List.class, int.class, int.class);
 JavaType keyT = JavaType.construct(String.class);
 JavaType valueT = JavaType.construct(String.class);
 MapLikeType.upgradeFrom(baseType, keyT, valueT);
 }

 @Test
 public void testNarrow() {
 MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 JavaType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 MapLikeType result = actual.narrow(Map.class);
 assertEquals(expected, result);
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = JavaType.construct(long.class);
 MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, newKeyType, valueType);
 MapLikeType result = actual.withKeyType(newKeyType);
 assertEquals(expected, result);
 }

 @Test
 public void testWithContentType() {
 JavaType newValueType = JavaType.construct(double.class);
 MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, newValueType);
 MapLikeType result = actual.withContentType(newValueType);
 assertEquals(expected, result);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType);
 MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 "_bogusSuperClass(Map.class)", null, keyType, valueType, handler, null, false);
 MapLikeType result = actual.withTypeHandler(handler);
 assertEquals(expected, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType, null, handler, false); MapLikeType result = actual.withContentTypeHandler(handler); assertEquals(expected, result); }

 @Test public void testWithValueHandler() { Object handler = new Object(); MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType, handler, null, false); MapLikeType result = actual.withValueHandler(handler); assertEquals(expected, result); }

 @Test public void testWithContentValueHandler() { Object handler = new Object(); MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType, null, handler, false); MapLikeType result = actual.withContentValueHandler(handler); assertEquals(expected, result); }

 @Test public void testWithStaticTyping() { MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType, null, null, false); MapLikeType expected = new MapLikeType(Map.class, TypeBindings.emptyBindings(), Map.class, null, keyType, valueType, null, null, true); MapLikeType result = actual.withStaticTyping(); assertEquals(expected, result); }

 @Test public void testRefine() { JavaType rawType = JavaType.construct(HashMap.class, int.class, int.class); TypeBindings bindings = TypeBindings.create(HashMap.class, keyType, valueType); JavaType superClass = JavaType.construct(Map.class); JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) }; MapLikeType actual = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType expected = new MapLikeType(HashMap.class, bindings, superClass, superInterfaces, keyType, valueType, null, null, false); MapLikeType result = actual.refine(rawType, bindings, superClass, superInterfaces); assertEquals(expected, result); }

 @Test public void testBuildCanonicalName() { Class<?> clazz = Map.class; JavaType key = JavaType.construct(int.class, int.class); JavaType value = JavaType.construct(String.class); String actual = mapType.buildCanonicalName(); String expected = String.format("[map-like type; class %s, %s -> %s]", clazz.getName(), key.toString(), value.toString()); assertEquals(expected, actual); }

 @Test public void testToString() { Class<?> clazz = Map.class; JavaType key = JavaType.construct(int.class, int.class); JavaType value = JavaType.construct(String.class); String actual = mapType.toString(); String expected = String.format("[map-like type; class %s, %s -> %s]", clazz.getName(), key.toString(), value.toString()); assertEquals(expected, actual); }

 @Test public void testEquals() { MapLikeType mapType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType mapType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType mapType3 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); assertTrue(mapType1.equals(mapType2)); assertFalse(mapType1.equals(mapType3)); }

 @Test public void testEqualsWithSameInstance() { MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); assertTrue(mapType.equals(mapType)); }

 @Test public void testEqualsWithDifferentClasses() { MapLikeType mapType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); MapLikeType mapType2 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); assertFalse(mapType1.equals(mapType2)); }

 @Test public void testEqualsWithDifferentKeyTypes() { MapLikeType mapType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, JavaType.construct(int.class, int.class), valueType); MapLikeType mapType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, JavaType.construct(long.class), valueType); assertFalse(mapType1.equals(mapType2)); }

 @Test public void testEqualsWithDifferentValueTypes() { MapLikeType mapType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, JavaType.construct(int.class, int.class)); MapLikeType mapType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, JavaType.construct(long.class)); assertFalse(mapType1.equals(mapType2)); }

 @Test public void testGetErasedSignature() { StringBuilder sb = new StringBuilder(); MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); StringBuilder expected = new StringBuilder("[Ljava.util.Map;"); StringBuilder actual = mapType.getErasedSignature(sb); assertEquals(expected.toString(), actual.toString()); }

 @Test public void testGetGenericSignature() { StringBuilder sb = new StringBuilder(); MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), "_bogusSuperClass(Map.class)", null, keyType, valueType); StringBuilder expected = new StringBuilder("[Ljava.util.Map;<Ljava.lang.Integer;Ljava.lang.Integer;>Ljava.lang.Object;"); StringBuilder actual = mapType.getGenericSignature(sb); assertEquals(expected.toString(), actual.toString()); }
}