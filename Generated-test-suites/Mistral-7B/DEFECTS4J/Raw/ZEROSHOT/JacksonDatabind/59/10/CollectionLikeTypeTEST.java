package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructorWithClassAndBindings() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.standard(), new SimpleType(Integer.class), null, null);
 assertEquals(Integer.class, collectionLikeType.getElementType().getRawClass());
 }

 @Test
 public void testConstructorWithBaseAndElementType() {
 TypeBase baseType = new TypeBase(Integer[].class, null, null, null, null, null, null, false);
 CollectionLikeType collectionLikeType = new CollectionLikeType(baseType, new SimpleType(Integer.class));
 assertEquals(Integer.class, collectionLikeType.getElementType().getRawClass());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = new SimpleType(ArrayList.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, new SimpleType(Integer.class));
 assertEquals(Integer.class, collectionLikeType.getElementType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromWithInvalidBaseType() {
 JavaType javaType = new SimpleType(String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, new SimpleType(Integer.class));
 fail("Should not reach here");
 }

 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertEquals(new SimpleType(Integer.class), collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertEquals(null, collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertFalse(collectionLikeType.hasHandlers());
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testEquals() {
 CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 CollectionLikeType collectionLikeType3 = new CollectionLikeType(List.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 }

 @Test
 public void testToString() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.standard(), null, null, new SimpleType(Integer.class), null, null, false);
 assertEquals("[collection-like type; class java.util.Collection, contains class java.lang.Integer]", collectionLikeType.toString());
 }
}