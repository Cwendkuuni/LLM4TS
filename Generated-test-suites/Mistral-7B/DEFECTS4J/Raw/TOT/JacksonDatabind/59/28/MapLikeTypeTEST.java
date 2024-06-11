package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Test;

/**
 * Test cases for MapLikeType class.
 */
public class MapLikeTypeTest {

 // Test constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, JavaType keyType, JavaType valueType
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(Map.class, mapLikeType.getRawType());
 assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
 assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
 }

 // Test constructor with TypeBase, JavaType keyType, JavaType valueType
 @Test
 public void testMapLikeTypeUpgrade() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeVariable.of(String.class), TypeVariable.of(Integer.class));
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(Map.class, mapLikeType.getRawType());
 assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
 assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
 }

 // Test factory method that can be used to "upgrade" a basic type into collection-like one; usually done via TypeModifier
 @Test
 public void testUpgradeFrom() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeVariable.of(String.class), TypeVariable.of(Integer.class));
 MapLikeType mapLikeType = new MapLikeType(listType, String.class, Integer.class);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(Map.class, mapLikeType.getRawType());
 assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
 assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
 }

 // Test getKeyType()
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
 }

 // Test getValueType()
 @Test
 public void testGetValueType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
 }

 // Test isContainerType()
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test isMapLikeType()
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test getErasedSignature()
 @Test
 public void testGetErasedSignature() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("[map-like type; class java.util.Map, null -> null]", mapLikeType.getErasedSignature().toString());
 }

 // Test getGenericSignature()
 @Test
 public void testGetGenericSignature() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/Integer;>]", mapLikeType.getGenericSignature().toString());
 }

 // Test equals()
 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType mapLikeType3 = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, String.class, Integer.class);
 assertTrue(mapLikeType1.equals(mapLikeType2));
 assertFalse(mapLikeType1.equals(mapLikeType3));
 }

 // Test toString()
 @Test
 public void testToString() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapLikeType.toString());
 }
}