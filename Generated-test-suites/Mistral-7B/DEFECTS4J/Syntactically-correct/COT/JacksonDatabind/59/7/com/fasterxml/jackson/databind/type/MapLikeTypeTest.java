package com.example.mapliketype; // replace with your package name

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MapLikeTypeTest {

 private JavaType keyType = new SimpleType("java.lang.String").wrap();
 private JavaType valueType = new SimpleType("java.lang.Integer").wrap();

 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test
 public void testMapLikeTypeUpgradeFrom() {
 JavaType baseType = new SimpleType("java.util.HashMap").wrap();
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 }

 @Test
 public void testMapLikeTypeWithKeyType() {
 JavaType newKeyType = new SimpleType("java.lang.Integer").wrap();
 MapLikeType mapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals(newKeyType, mapLikeType.getKeyType());
 }

 @Test
 public void testMapLikeTypeWithContentType() {
 JavaType newValueType = new SimpleType("java.lang.Double").wrap();
 MapLikeType mapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals(newValueType, mapLikeType.getValueType());
 }

 @Test
 public void testMapLikeTypeWithTypeHandler() {
 Object handler = new Object();
 MapLikeType mapLikeType = mapLikeType.withTypeHandler(handler);
 assertNotNull(mapLikeType.getTypeHandler());
 }

 @Test
 public void testMapLikeTypeWithContentTypeHandler() {
 Object handler = new Object();
 MapLikeType mapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertNotNull(mapLikeType.getValueHandler());
 }

 @Test
 public void testMapLikeTypeWithValueHandler() {
 Object handler = new Object();
 MapLikeType mapLikeType = mapLikeType.withValueHandler(handler);
 assertNotNull(mapLikeType.getValueHandler());
 }

 @Test
 public void testMapLikeTypeWithContentValueHandler() {
 Object handler = new Object();
 MapLikeType mapLikeType = mapLikeType.withContentValueHandler(handler);
 assertNotNull(mapLikeType.getValueHandler());
 }

 @Test
 public void testMapLikeTypeWithStaticTyping() {
 MapLikeType mapLikeType = mapLikeType.withStaticTyping();
 assertTrue(mapLikeType.isStaticTyping());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapLikeTypeInvalidUpgrade() {
 JavaType baseType = new SimpleType("java.lang.String").wrap();
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 try {
 MapLikeType mapLikeType2 = mapLikeType.upgradeFrom(new SimpleType("java.util.List")
 .wrap(), keyType, valueType);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testMapLikeTypeIsContainerType() {
 assertTrue(mapLikeType.isContainerType());
 }

 @Test
 public void testMapLikeTypeIsMapLikeType() {
 assertTrue(mapLikeType.isMapLikeType());
 }

 @Test
 public void testMapLikeTypeIsTrueMapType() {
 assertTrue(mapLikeType.isTrueMapType());
 }

}